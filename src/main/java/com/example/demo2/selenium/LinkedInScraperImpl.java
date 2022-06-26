package com.example.demo2.selenium;

import com.example.demo2.company.model.Address;
import com.example.demo2.company.model.Company;
import com.example.demo2.company.model.Post;
import com.example.demo2.company.repository.CompanyRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo2.utils.JSONUtil.JsonObjectGetIntItem;
import static com.example.demo2.utils.JSONUtil.JsonObjectGetStringItem;

@Service
@Slf4j
@AllArgsConstructor
public class LinkedInScraperImpl implements LinkedInScraper {
    ChromeScraper chromeScraper;
    DriverSupplier driverSupplier;

    CompanyRepository companyRepository;


    public void shutDownDriver() {
        chromeScraper.shutDownDriver();
    }

    public boolean login(String email, String password) {


        boolean isSuccessfull = false;

        try {
            WebDriver driver = chromeScraper.getDriver();
            // opening login page
            driver.get("https://www.linkedin.com/login");
            log.info("Attempting Login");

            // wait until login page is loaded
            WebElement emailField = new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id,'username')]")));

            // email field is already fetched above, below will fetch password field
            WebElement passwordField = driver.findElement(By.xpath("//input[contains(@id,'password')]"));

            // input username and password
            emailField.sendKeys(email);
            passwordField.sendKeys(password);

            //          passwordField.sendKeys(Keys.ENTER);

            // find login button and perform click
            WebElement loginBtn = driver
                    .findElement(By.xpath("//button[contains(@type,'submit')]"));
            loginBtn.click();

            // wait until home page is visible
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//nav[contains(@class, 'global-nav__nav')]")));

            log.info("Login..................................................................[Success]");
            isSuccessfull = true;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Login Faild.");
            log.error("Possible Error: Wrong Username and Password.");
            log.error("\nPossible Error: Captcha Verification is preventing login.\n");
            log.error("Possible Error: Page is taking more then 20 seconds to load.\n");
        }

        return isSuccessfull;

    }

    public boolean moveToURL(String postURL) {
        boolean isFound = false;
        try {
            log.info("Opening Post URL");
            WebDriver driver = chromeScraper.getDriver();
            driver.get(postURL);
            // wait till post loads up
            new WebDriverWait(driver, Duration.ofSeconds(60)).until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//*[contains(@class, 'full-width')]")));

            // check for possible error
            if (driver.findElements(By.xpath("//section[contains(@class, 'feed-shared-error')]")).size() > 0) {
                log.info("Error opening Post URL");
            }

            isFound = true;

        } catch (Exception e) {
            log.info("Error opening Post URL");
            e.printStackTrace();
        }
        return isFound;
    }

    JsonObject getCompanyNode(JsonArray include, String url) {
        for (JsonElement jsonElement : include) {
            JsonObject next = jsonElement.getAsJsonObject();
            if ("com.linkedin.voyager.dash.organization.Company".equals(next.get("$type").getAsString()) && url.equals(next.get("url").getAsString())) {
                return next;
            }
        }
        throw new IllegalStateException("Company Data not found");
    }

    Company findOrCreateCompany(String url) {
        return companyRepository.findByLinkedinURL(url).orElseGet(Company::new);
    }

    static String searchTagCode(WebDriver driver, String contain) {
        JavascriptExecutor j = (JavascriptExecutor) driver;

        return (String) j.executeScript(String.format("function getCode() {for (const code of document.querySelectorAll('code')) {if (code.textContent.includes('%s')) {return code.textContent;}} }  return getCode();", contain));
    }

    Optional<Integer> scrapeJobsOpen(String code) {
        if (!StringUtils.hasText(code)) {
            return Optional.empty();
        }
        JsonElement root = JsonParser.parseString(code);
        JsonObject data = root.getAsJsonObject().get("data").getAsJsonObject();
        if (data.has("paging")) {
            JsonObject paging = data.get("paging").getAsJsonObject();
            return Optional.of(paging.get("total").getAsInt());
        }
        return Optional.empty();
    }

    Optional<Post> scrapeLastPost(String code) {
        Post post = new Post();
        if (!StringUtils.hasText(code)) {
            return Optional.empty();
        }
        JsonElement root = JsonParser.parseString(code);
        JsonArray include = root.getAsJsonObject().get("included").getAsJsonArray();
        for (JsonElement jsonElement : include) {
            JsonObject next = jsonElement.getAsJsonObject();
            if ("com.linkedin.voyager.feed.render.UpdateV2".equals(next.get("$type").getAsString())) {

                if (next.has("commentary") && !next.get("commentary").isJsonNull()) {
                    JsonObject commentary = next.get("commentary").getAsJsonObject();
                    post.setOriginalLanguage(JsonObjectGetStringItem(commentary, "originalLanguage").orElse(null));
                    if (commentary.has("text")) {
                        JsonObject textCommentary = commentary.get("text").getAsJsonObject();
                        post.setText(JsonObjectGetStringItem(textCommentary, "text").orElse(null));
                        return Optional.of(post);
                    }
                    break;

                }
            }
        }
        return Optional.empty();
    }

    Address scrapeAddress(JsonObject comp) {
        Address address = new Address();
        JsonElement headquarter = comp.get("headquarter");
        if (headquarter != null && headquarter.getAsJsonObject().has("address")) {
            JsonObject addressEl = headquarter.getAsJsonObject().get("address").getAsJsonObject();

            address.setCountry(JsonObjectGetStringItem(addressEl, "country").orElse(null));
            address.setCity(JsonObjectGetStringItem(addressEl, "city").orElse(null));
            address.setPostalCode(JsonObjectGetStringItem(addressEl, "postalCode").orElse(null));
            address.setAddress(JsonObjectGetStringItem(addressEl, "line1").orElse(null));

        }
        return address;
    }

    public Optional<Company> scrapeCompany(Company company, String url) {
        log.info("Loading Company");
        WebDriver driver = chromeScraper.getDriver();


        String code = searchTagCode(driver, "VIEW_WEBSITE");
        try {
            JsonElement root = JsonParser.parseString(code);
            JsonArray include = root.getAsJsonObject().get("included").getAsJsonArray();

            JsonObject comp = getCompanyNode(include, url);

            company.setName(JsonObjectGetStringItem(comp, "name").orElse(null));
            company.setWebsiteUrl(JsonObjectGetStringItem(comp, "websiteUrl").orElse(null));

            company.setLinkedinURL(url);
            company.setDescription(JsonObjectGetStringItem(comp, "description").orElse(null));


            company.setAddress(scrapeAddress(comp));

            company.setEmployeeCount(JsonObjectGetIntItem(comp, "employeeCount").orElse(null));

            JsonElement specialities = comp.get("specialities");
            List<String> specs = new ArrayList<>();
            company.setSpecialities(specs);
            if (specialities != null) {
                JsonArray specialitiesArr = specialities.getAsJsonArray();
                for (JsonElement item : specialitiesArr) {
                    specs.add(item.getAsString());
                }
            }

            String postUrl = url.concat(url.endsWith("/") ? "posts/" : "/posts/");
            moveToURL(postUrl);
            code = searchTagCode(driver, "com.linkedin.voyager.feed.render.UpdateV2");

            Optional<Post> optPost = scrapeLastPost(code);

            if (optPost.isPresent()) {
                Post post = optPost.get();
                post.setCompany(company);
                company.setPost(post);
            } else {
                company.setPost(null);
            }


            Optional<String> jobSearchUrl = JsonObjectGetStringItem(comp, "jobSearchUrl");
            company.setOpenJobs(0);
            if (jobSearchUrl.isPresent()) {
                moveToURL(jobSearchUrl.get());

                code = searchTagCode(driver, "com.linkedin.voyager.jobs.JobPosting");
                Optional<Integer> optJobs = scrapeJobsOpen(code);
                optJobs.ifPresent(company::setOpenJobs);

            }


            return Optional.of(company);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Company> scrapeCompanyAndSave(String url) {


        Company company = findOrCreateCompany(url);
        Optional<Company> companyUpd = scrapeCompany(company, url);
        companyUpd.ifPresent(value -> companyRepository.save(value));
        return companyUpd;


    }

}
