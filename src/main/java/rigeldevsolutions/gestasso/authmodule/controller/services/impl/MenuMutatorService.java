package rigeldevsolutions.gestasso.authmodule.controller.services.impl;

import rigeldevsolutions.gestasso.authmodule.controller.repositories.MenuRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.PrvRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IMenuMutatorService;
import rigeldevsolutions.gestasso.authmodule.model.constants.AuthActions;
import rigeldevsolutions.gestasso.authmodule.model.constants.AuthTables;
import rigeldevsolutions.gestasso.authmodule.model.dtos.menu.CreateMenuDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.menu.MenuMapper;
import rigeldevsolutions.gestasso.authmodule.model.dtos.menu.PrvToMenuDTO;
import rigeldevsolutions.gestasso.authmodule.model.entities.Menu;
import rigeldevsolutions.gestasso.modulelog.controller.service.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MenuMutatorService implements IMenuMutatorService
{
    private final MenuRepo menuRepo;
    private final PrvRepo prvRepo;
    private final MenuMapper menuMapper;
    private final ILogService logger;

    @Override @Transactional
    public Menu createMenu(CreateMenuDTO dto) throws UnknownHostException {
        Menu menu = menuMapper.mapToMenu(dto);
        String codeChain = Arrays.stream(dto.getPrvsCodes())
                .filter(code->!menuRepo.menuHasPrivilege(dto.getMenuCode(), code) && prvRepo.existsByCode(code))
                .collect(Collectors.joining(Menu.chainSeparator));
        menu.setPrvsCodesChain(codeChain);
        menu = menuRepo.save(menu);
        logger.logg(AuthActions.CREATE_MENU, null, menu, AuthTables.MENU_TABLE, null);
        return menu;
    }

    @Override @Transactional
    public void addPrvToMenu(PrvToMenuDTO dto) throws UnknownHostException
    {
        Menu menu = menuRepo.findByMenuCode(dto.getMenuCode());
        Menu oldMenu = new Menu();
        BeanUtils.copyProperties(menu, oldMenu);

        String codeChain = Arrays.stream(dto.getPrvCodes())
                .filter(code->!menuRepo.menuHasPrivilege(dto.getMenuCode(), code) && prvRepo.existsByCode(code))
                .collect(Collectors.joining(Menu.chainSeparator));
        if(codeChain == null || codeChain.trim().equals("")) return;
        menu.setPrvsCodesChain(menu.getPrvsCodesChain() + Menu.chainSeparator + codeChain);
        menu = menuRepo.save(menu);
        logger.logg(AuthActions.ADD_PRV_TO_MENU, oldMenu, menu, "menu", null);
    }

    @Override
    public void removePrvToMenu(PrvToMenuDTO dto) throws UnknownHostException
    {
        Menu menu = menuRepo.findByMenuCode(dto.getMenuCode());
        String codeChain = menu.getPrvsCodesChain();

        if(codeChain == null || codeChain.trim().equals("")) return;
        Menu oldMenu = new Menu();
        BeanUtils.copyProperties(menu, oldMenu);

        Set<String> prvCodesToRemove = Arrays.stream(dto.getPrvCodes())
                .filter(code->menuRepo.menuHasPrivilege(dto.getMenuCode(), code) && prvRepo.existsByCode(code))
                .collect(Collectors.toSet());
        if(codeChain == null || codeChain.trim().equals("")) return;

        menu.setPrvsCodesChain(Arrays.stream(codeChain.split(Menu.chainSeparator)).filter(code->!prvCodesToRemove.contains(code)).collect(Collectors.joining(Menu.chainSeparator)));
        menu = menuRepo.save(menu);
        logger.logg(AuthActions.RMV_PRV_TO_MENU, oldMenu, menu, "menu", null);
    }
}
