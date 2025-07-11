package org.example.pages;

import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import net.serenitybdd.core.pages.WebElementFacade;
import java.util.stream.Collectors;

import net.serenitybdd.core.annotations.findby.FindBy;

import net.thucydides.core.pages.PageObject;

import java.util.List;

//@DefaultUrl("http://en.wiktionary.org/wiki/Wiktionary")
@DefaultUrl("http://www.cs.ubbcluj.ro")
public class DictionaryPage extends PageObject {

    @FindBy(id="s")
    private WebElementFacade searchTerms;

    @FindBy(name="go")
    private WebElementFacade lookupButton;

    public void enter_keywords(String keyword) {

        //searchTerms.type(keyword);
        searchTerms.typeAndEnter(keyword);
    }

    public void lookup_terms() {

        //lookupButton.click();
    }

    public List<String> getDefinitions() {

        WebElementFacade searchList = find(By.className("post-wrap"));
       // return searchList.findElements(By.id("post-57228")).stream().map(element->element.getText()).collect(Collectors.toList());
        return searchList.findElements(By.className("post")).stream().map(element->element.getText()).collect(Collectors.toList());

        /*
        WebElementFacade definitionList = find(By.tagName("ol"));
        return definitionList.findElements(By.tagName("li")).stream()
                .map( element -> element.getText() )
                .collect(Collectors.toList());
      */


    }
}