package rigeldevsolutions.gestasso.grademodule.controller.service;

import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;

import java.util.List;

public interface ICategorieService
{
    List<Categorie> getAllCategories();
    long getRang(String catName);
}
