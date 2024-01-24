package rigeldevsolutions.gestasso.grademodule.model.enums;

import org.apache.commons.lang3.EnumUtils;

public enum Categorie
{
    A(1),
    B(2),
    C(3),
    D(4),
    E(5);

    public static boolean exists(String catName)
    {
        return EnumUtils.isValidEnum(Categorie.class, catName);
    }

    public boolean isGreaterThan(Categorie cat)
    {
        return this.rang < cat.rang ;
    }

    public boolean isLessThan(Categorie cat)
    {
        return this.rang > cat.rang ;
    }

    public boolean isGreaterThanEqual(Categorie cat)
    {
        return this.rang <= cat.rang ;
    }

    public boolean isLessThanEqual(Categorie cat)
    {
        return this.rang >= cat.rang ;
    }

    public long rang;
    Categorie(long rang)
    {
        this.rang = rang;
    }
}
