package rigeldevsolutions.gestasso.authmodule.controller.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.FunctionRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IMenuReaderService;
import rigeldevsolutions.gestasso.authmodule.model.constants.SecurityConstants;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appuser.AuthResponseDTO;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppFunction;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import rigeldevsolutions.gestasso.modulelog.model.dtos.response.JwtInfos;
import rigeldevsolutions.gestasso.modulelog.model.entities.Log;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.HttpServletManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class JwtService implements IJwtService
{
    private final FunctionRepo functionRepo;
    private final UserRepo userRepo;
    private final IMenuReaderService menuService;

    @Override
    public AuthResponseDTO generateJwt(UserDetails userDetails, String connectionId)
    {
        String userEmail = userDetails.getUsername();
        AppUser user = userRepo.findByEmail(userEmail).orElseThrow(()->new AppException("Utilisateur introuvable"));
        Long assoId = functionRepo.getCurrentFncAssoId(userDetails.getUsername());
        Long sectionId = functionRepo.getCurrentFncSectionId(userDetails.getUsername());
        List<Long> functionIds = functionRepo.getCurrentFncIds(user.getUserId().longValue());
        Long functionId = functionIds == null || functionIds.size() != 1 ? null : new ArrayList<>(functionIds).get(0);
        Map<String, Object> extraClaims = new HashMap<>(); //functionId = 1l;

        AppFunction function = functionId == null ? null : functionRepo.findById(functionId).orElse(null);

        Set<String> menus = menuService.getMenusByFncId(functionId);
        Set<String> authorities = userDetails.getAuthorities().stream().map(auth->auth.getAuthority()).collect(Collectors.toSet());
        Set<String> authAndMenus = new HashSet<String>(authorities);
        authAndMenus.addAll(menus);

        Long userId = user.getUserId();
        extraClaims.put("userId", userId);
        extraClaims.put("email", userEmail);
        extraClaims.put("nom", user.getFirstName());
        extraClaims.put("tel", user.getTel());
        extraClaims.put("prenom", user.getLastName());
        extraClaims.put("authorities", authAndMenus);
        extraClaims.put("menus", menus);
        extraClaims.put("assoId", assoId );
        extraClaims.put("sectionId", sectionId );
        extraClaims.put("functionId", functionId);
        extraClaims.put("connectionId", connectionId);
        extraClaims.put("assoName", function == null || function.getAssociation() == null ? null : function.getAssociation().getAssoName());
        extraClaims.put("sectionName", function == null || function.getSection() == null ? null : function.getSection().getSectionName());

        extraClaims.put("strId", function == null || function.getSection() == null || function.getSection().getStrTutelle() == null ? null : function.getSection().getStrTutelle().getStrId());
        extraClaims.put("strCode", function == null || function.getSection() == null || function.getSection().getStrTutelle() == null ? null : function.getSection().getStrTutelle().getStrCode());
        extraClaims.put("strName", function == null || function.getSection() == null || function.getSection().getStrTutelle() == null ? null : function.getSection().getStrTutelle().getStrName());
        extraClaims.put("strSigle", function == null || function.getSection() == null || function.getSection().getStrTutelle() == null ? null : function.getSection().getStrTutelle().getStrSigle());

        extraClaims.put("functionName", function == null ? null : function.getName());

        extraClaims.put("functionStartingDate", function == null ? null : Date.from(function.getStartsAt().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        extraClaims.put("functionEndingDate", function == null ? null : Date.from(function.getEndsAt().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        boolean hasCurrentFonction = functionId != null && functionRepo.functionIsCurrentForUser(functionId, userId);
        boolean doesNotHaveAnyFunction = !functionRepo.userHasAnyAppFunction(userId);
        AuthResponseDTO authResponseDTO = generateJwt(userEmail, extraClaims);
        authResponseDTO.setUser(user);
        return authResponseDTO;
    }

    @Override
    public AuthResponseDTO generateJwt(String username, Map<String, Object> extraClaims)
    {
        String accessToken = Jwts.builder().setClaims(extraClaims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.ACCESS_TOKEN_DURATION ))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256).compact();

        String refreshToken = Jwts.builder().setSubject(username)
                .claim("connectionId", extraClaims.get("connectionId"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_TOKEN_DURATION ))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256).compact();
        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Override
    public String extractUsername()
    {
        String extractedUsername = this.extractUsername(this.getCurrentJwt());
        return extractedUsername == null || extractedUsername.trim().equals("") ? "UNKNOWN" : extractedUsername;
    }

    @Override
    public String extractConnectionId()
    {
        Claims claims= this.extractAllClaims(this.getCurrentJwt());
        return claims == null ? "" : claims.get("connectionId", String.class);
    }

    @Override
    public String extractUsername(String jwt)
    {
        return this.extractClaim(jwt == null ? "" : jwt, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {
        return claimResolver.apply(this.extractAllClaims(jwt));
    }

    private Key getSigningKey()
    {
        byte[] keyBytes = Base64.getDecoder().decode(SecurityConstants.SEC_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String jwt)
    {
        if(jwt == null || jwt.trim().equals("")) return new DefaultClaims();
        return Jwts.parserBuilder().setSigningKey(this.getSigningKey()).build().parseClaimsJws(jwt).getBody();
    }

    @Override
    public Log getUserInfosFromJwt(String token)
    {
        Log log = new Log();
        Claims claims= this.extractAllClaims(token);
        Long  functionId = claims.get("functionId", Long.class);
        log.setUserEmail(this.extractUsername(token));
        log.setUserId(claims.get("userId", Long.class));
        log.setFunction(functionId == null ? null : functionRepo.findById(functionId).orElse(null));
        log.setConnectionId(claims.get("connectionId", String.class));
        return log;
    }

    @Override
    public Log getUserInfosFromJwt()
    {
        return this.getUserInfosFromJwt(this.getCurrentJwt());
    }

    @Override
    public JwtInfos getJwtInfos() {
        return this.getJwtInfos(this.getCurrentJwt());
    }

    @Override
    public JwtInfos getJwtInfos(String jwt)
    {
        JwtInfos jwtInfos = new JwtInfos();
        Claims claims= this.extractAllClaims(jwt);
        Long  functionId = claims.get("functionId", Long.class);
        AppFunction function = functionId == null ? null : functionRepo.findById(functionId).orElse(null);

        jwtInfos.setFncId(functionId);
        jwtInfos.setFncName(function == null ? "" : function.getName());
        jwtInfos.setUserEmail(this.extractUsername(jwt));
        jwtInfos.setUserId(claims.get("userId", Long.class));
        jwtInfos.setAssoId(claims.get("assoId", Long.class));
        jwtInfos.setAssoName(claims.get("assoName", String.class));
        jwtInfos.setSectionId(claims.get("sectionId", Long.class));
        jwtInfos.setSectionName(claims.get("sectionName", String.class));
        jwtInfos.setStrId(claims.get("strId", Long.class));
        jwtInfos.setStrName(claims.get("strName", String.class));
        jwtInfos.setStrCode(claims.get("strCode", String.class));
        jwtInfos.setStrSigle(claims.get("strSigle", String.class));
        jwtInfos.setAuthorities(claims.get("authorities", List.class));
        jwtInfos.setConnectionId(claims.get("connectionId", String.class));
        jwtInfos.setTokenStartingDate(this.extractClaim(jwt,Claims::getIssuedAt));
        jwtInfos.setTokenEndingDate(this.extractClaim(jwt,Claims::getExpiration));
        jwtInfos.setFncStartingDate(function == null ? null : function.getStartsAt());
        jwtInfos.setFncEndingDate(function == null ? null : function.getEndsAt());

        return jwtInfos;
    }

    @Override
    public String getCurrentJwt()
    {
        HttpServletRequest request = HttpServletManager.getCurrentHttpRequest();
        if(request == null) return null;
        return request.getHeader("Authorization") == null ? null : request.getHeader("Authorization").substring("Bearer ".length());
    }

    @Override
    public Object getClaim(String claimName)
    {
        return this.extractAllClaims(this.getCurrentJwt()).get(claimName);
    }

    @Override
    public Long getConnectedUserId() {
        Object userId = this.getClaim("userId");
        return userId == null ? null : Long.valueOf(String.valueOf(userId));
    }

    @Override
    public Long getConnectedUserFunctionId() {
        Object functionId = this.getClaim("functionId");
        return functionId == null ? null : Long.valueOf(String.valueOf(functionId));
    }


    @Override
    public boolean hasAnyAuthority(String... auth)
    {
        if(auth == null) return false;
        Set<String> authList = Arrays.stream(auth).collect(Collectors.toSet());
        Set<String> userAuthorities = this.getAuthorities();
        boolean hasAnyAuthority =userAuthorities.stream().anyMatch(authList::contains);
        return hasAnyAuthority;
    }

    @Override
    public Set<String> getAuthorities()
    {
        Object authorities = this.getClaim("authorities");
        return authorities == null ? new HashSet<>() : new HashSet<>((List<String>) authorities);
    }

    class TokenStatus
    {
        private int status;
        private String description;
        private String message;
    }
}