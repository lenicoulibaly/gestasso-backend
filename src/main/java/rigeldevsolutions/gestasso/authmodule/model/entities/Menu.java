package rigeldevsolutions.gestasso.authmodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "menu")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Menu
{
    @Id
    @Column(name = "menu_code")
    private String menuCode;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "prvs_codes_chain", length = 4000)
    private String prvsCodesChain;
    @Transient
    private List<String> prvsCodes;
    @Transient
    public static final String chainSeparator = "::";

    public List<String> getPrvsCodes()
    {
        if(this.prvsCodesChain == null) return new ArrayList<>();
        return Arrays.asList(this.prvsCodesChain.split(Menu.chainSeparator));
    }

    public Menu(String menuCode, String name, String prvsCodesChain) {
        this.menuCode = menuCode;
        this.name = name;
        this.prvsCodesChain = prvsCodesChain;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class MenuResp
    {
        private String menuCode;
        private String name;
    }
}
