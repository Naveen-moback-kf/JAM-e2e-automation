package com.JobMapping.utils.API;

import com.JobMapping.utils.common.CommonVariable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class kfApi {
    private static String token;
    private static final String uuid = UUID.randomUUID().toString();
    private static final String baseProfile = "Robot Process Automation Analyst";
    private static final String baseProfileForUrl = "Robot%20Process%20Automation%20Analyst";

    public static void createJob() throws IOException, InterruptedException {
        JSONObject successProfile = successProfile();
        JSONArray jobFactors = jobFactors();
        String jobName = "QA-" + System.currentTimeMillis();
        String body = formBody(successProfile, jobFactors, jobName);
        HttpRequest request = formPostRequest("hrms/architect/jobs", body);
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        
        // Log the response for debugging/verification purposes
        System.out.println("Job creation response status: " + response.statusCode());
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            System.err.println("Job creation failed with status: " + response.statusCode() + ", Response: " + response.body());
        }
    }

    public static void clearJobs() throws IOException, InterruptedException {
        List<Integer> jobsToDelete;

        do {
            jobsToDelete = new ArrayList<>();

            HttpRequest request = formGetRequest("hrms/architect/jobs/?outputType=&type=SEARCH_JOBS");
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            JSONArray jobArray = json.getJSONObject("data").getJSONArray("jobs");

            for (int i = 0; i < jobArray.length(); i++) {
                JSONObject job = jobArray.getJSONObject(i);
                String title = job.getString("title");

                if (title.contains("QA-1"))
                    jobsToDelete.add(job.getInt("id"));
            }

            for (Integer integer : jobsToDelete) {
                HttpRequest deleteRequest = formDeleteRequest("hrms/architect/jobs/" + integer);
                HttpClient.newHttpClient().send(deleteRequest, HttpResponse.BodyHandlers.ofString());
            }
        } while (!jobsToDelete.isEmpty());
    }

    private static String authenticate() throws IOException, InterruptedException {
        if (token != null && !token.isEmpty())
            return token;

        HttpRequest request = formAuthRequest();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        token = json.getJSONObject("data").getString("authToken");

        return token;
    }

    private static JSONObject successProfile() throws IOException, InterruptedException {
        HttpRequest request = formGetRequest("hrms/successprofiles/" + successProfileId());
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());

        return json.getJSONObject("data");
    }

    private static String successProfileId() throws IOException, InterruptedException {
        String result = "";
        HttpRequest request = formGetRequest("hrms/successprofiles/?searchString=" + baseProfileForUrl);
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray successProfilesList = new JSONObject(response.body()).getJSONObject("data").getJSONArray("jobs");

        for (int i = 0; i < successProfilesList.length(); i++) {
            JSONObject profile = successProfilesList.getJSONObject(i);
            String title = profile.getString("title");
            String id = profile.getString("id");

            if ((baseProfile + " I").equals(title)) {
                result = id;
            }
        }

        return !result.equals("") ? result : null;
    }

    private static JSONArray jobFactors() throws IOException, InterruptedException {
        JSONObject successProfile = successProfile();
        String jobRoleTypeId = successProfile.getString("jobRoleTypeId");
        int successProfileId = successProfile.getInt("id");
        String url = "hrms/successprofiles/actions/jobfactors?jobRoleTypeId=" + jobRoleTypeId + "&successProfileId=" + successProfileId;
        HttpRequest request = formGetRequest(url);
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return new JSONObject(response.body()).getJSONArray("data");
    }

    private static HttpRequest formAuthRequest() throws IOException, InterruptedException {
        String url = "actions/login";
        String body = "{\"username\":\"" + CommonVariable.USERNAME + "\",\"password\": \"" + CommonVariable.PASSWORD + "\"}";

        return formRequest(url, body, false);
    }

    private static HttpRequest formGetRequest(String url) throws IOException, InterruptedException {
        return formRequest(url, null, true);
    }

    private static HttpRequest formPostRequest(String url, String body) throws IOException, InterruptedException {
        return formRequest(url, body, true);
    }

    private static HttpRequest formDeleteRequest(String url) throws IOException, InterruptedException {
        String uri = "https://testproductsapi.kornferry.com/" + "v1/" + url;

        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("ps-session-id", uuid)
                .header("Content-Type", "application/json")
                .header("authToken", authenticate())
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    private static HttpRequest formRequest(String url, String bodyContents, boolean needsAuthentication) throws IOException, InterruptedException {
        String uri = "https://testproductsapi.kornferry.com/" + "v1/" + url;
        String method;
        HttpRequest.BodyPublisher body;

        if (bodyContents != null) {
            method = "POST";
            body = HttpRequest.BodyPublishers.ofString(bodyContents);
        } else {
            method = "GET";
            body = HttpRequest.BodyPublishers.noBody();
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("ps-session-id", uuid)
                .header("Content-Type", "application/json");

        if (needsAuthentication)
            requestBuilder.header("authToken", authenticate());

        HttpRequest.Builder request = requestBuilder.method(method, body);

        return request.build();
    }

    private static String formBody(JSONObject successProfile, JSONArray jobFactorsData, String jobName) {
        JSONArray jobFactorsValues = new JSONArray();
        JSONArray assessmentValues = new JSONArray();
        JSONArray sectionsData = new JSONArray();
        JSONArray traitsData = new JSONArray();
        JSONArray driversData = new JSONArray();
        JSONArray assessments = successProfile.getJSONArray("assessments");
        JSONArray sections = successProfile.getJSONArray("sections");
        JSONObject traitsAndDrivers = successProfile.getJSONObject("traitsAndDrivers");
        JSONArray traits = traitsAndDrivers.getJSONArray("roleRequirementsQuestions");
        JSONArray drivers = traitsAndDrivers.getJSONArray("driverCultureRankings");

        if (jobFactorsData != null) {
            for (int i = 0; i < jobFactorsData.length(); i++) {
                JSONObject jobFactor = jobFactorsData.getJSONObject(i);
                Object id = jobFactor.get("id");
                boolean valueExists = jobFactor.has("value");
                int value = 0;

                if (valueExists && jobFactor.get("value") != JSONObject.NULL) {
                    value = jobFactor.getBoolean("value") ? 1 : 0;
                }

                JSONObject jobFactorData = new JSONObject();
                jobFactorData.put("id", id);
                jobFactorData.put("value", value);
                jobFactorsValues.put(jobFactorData);
            }
        }

        for (int i = 0; i < assessments.length(); i++) {
            JSONObject assessment = assessments.getJSONObject(i);
            Object id = assessment.get("id");
            boolean isMandatory;
            isMandatory = assessment.getBoolean("isMandatory");

            JSONObject assessmentData = new JSONObject();
            assessmentData.put("id", id);
            assessmentData.put("isMandatory", isMandatory);

            assessmentValues.put(assessmentData);
        }

        for (int i = 0; i < sections.length(); i++) {
            JSONObject section = sections.getJSONObject(i);
            JSONObject sectionData = new JSONObject();
            JSONArray subcategoriesData = new JSONArray();

            if (section.has("categories")) {
                JSONArray categories = section.getJSONArray("categories");

                for (int j = 0; j < categories.length(); j++) {
                    JSONObject category = categories.getJSONObject(j);
                    JSONArray subcategories = category.getJSONArray("subCategories");

                    for (int k = 0; k < subcategories.length(); k++) {
                        JSONObject subcategory = subcategories.getJSONObject(k);
                        JSONObject subcategoryData = new JSONObject();

                        subcategoryData.put("id", subcategory.get("id"));
                        subcategoryData.put("level", subcategory.getJSONArray("descriptions").getJSONObject(0).get("level"));
                        subcategoryData.put("userEdited", subcategory.get("userEdited"));

                        if (subcategory.has("type"))
                            subcategoryData.put("type", subcategory.get("type"));

                        if (subcategory.has("descriptions"))
                            if (subcategory.getJSONArray("descriptions").getJSONObject(0).has("score")) {
                                JSONObject description = subcategory.getJSONArray("descriptions").getJSONObject(0);

                                subcategoryData.put("score", description.get("score"));
                            }

                        if (subcategory.has("dependents"))
                            subcategoryData.put("dependents", subcategory.get("dependents"));

                        subcategoriesData.put(subcategoryData);
                    }
                }
            }

            sectionData.put("id", section.get("id"));


            if (!subcategoriesData.isEmpty())
                sectionData.put("subCategories", subcategoriesData);
                sectionsData.put(sectionData);
        }

        for (int i = 0; i < traits.length(); i++) {
            JSONObject trait = traits.getJSONObject(i);
            JSONObject traitData = new JSONObject();

            traitData.put("sliderValue", trait.get("sliderValue"));
            traitData.put("ucpCode", trait.get("ucpCode"));
            traitsData.put(traitData);
        }

        for (int i = 0; i < drivers.length(); i++) {
            JSONObject driver = drivers.getJSONObject(i);
            JSONObject driverData = new JSONObject();

            driverData.put("cultureCode", driver.get("cultureCode"));
            driverData.put("value", driver.get("value"));
            driversData.put(driverData);
        }

        JSONObject requestBody = new JSONObject();
        JSONObject job = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONArray roles = new JSONArray() {};
        JSONObject traitsAndDriverReq = new JSONObject();
        JSONObject role = new JSONObject();

        job.put("additionalComments", "");
        job.put("assessments", assessmentValues);
        job.put("cogAbilityType", successProfile.get("cogAbilityType"));
        job.put("companyDescription", "Kornferry");
        job.put("description", successProfile.get("description"));

        boolean enableJobAnalysisWorkflowExists = successProfile.has("enableJobAnalysisWorkflow");
        int enableJobAnalysisWorkflow = 0;

        if (enableJobAnalysisWorkflowExists && successProfile.opt("enableJobAnalysisWorkflow") != JSONObject.NULL)
            enableJobAnalysisWorkflow = successProfile.getBoolean("enableJobAnalysisWorkflow") ? 1 : 0;

        job.put("enableJobAnalysisWorkflow", enableJobAnalysisWorkflow);
        job.put("familyId", successProfile.get("familyId"));
        job.put("finalizedResponsibilities", 1);
        job.put("grade", successProfile.get("grade"));
        job.put("hayPoints", successProfile.get("hayPoints"));
        job.put("hideJobInPM", 0);
        job.put("hrLevel", successProfile.get("hrLevel"));
        job.put("isExecutive", successProfile.get("isExecutive"));
        job.put("isExecutiveProfileEdited", successProfile.get("isExecutiveProfileEdited"));
        job.put("isGlobalRole", successProfile.getJSONObject("regionalNormData").get("isGlobalRole"));
        job.put("jeLineGrade", successProfile.get("jeLineGrade"));
        job.put("jobCode", successProfile.get("jobCode"));
        job.put("normCountryId", successProfile.getJSONObject("regionalNormData").get("normCountryId"));
        job.put("normId", successProfile.getJSONObject("regionalNormData").get("normId"));
        job.put("parentJobId", successProfile.get("id"));
        properties.put("jobProperties", new JSONArray());
        job.put("properties", properties);
        job.put("sections", sectionsData);
        job.put("shortProfile", successProfile.get("shortProfile"));
        job.put("standardHayGrade", successProfile.getInt("standardHayGrade"));
        job.put("status", "DRAFT");
        job.put("subFamilyId", successProfile.get("subFamilyId"));
        job.put("title", jobName);
        traitsAndDriverReq.put("driversCultureRankings", driversData);
        traitsAndDriverReq.put("roleRequirementQA", traitsData);
        job.put("traitsAndDriverReq", traitsAndDriverReq);
        role.put("id", successProfile.get("id"));
        role.put("jobFactors", jobFactorsValues);
        role.put("jobRoleTypeId", successProfile.get("jobRoleTypeId"));
        role.put("standardHayGrade", successProfile.getInt("standardHayGrade"));
        roles.put(role);
        job.put("roles", roles);
        requestBody.put("job", job);
        requestBody.put("spId", successProfile.get("id"));

        return requestBody.toString();
    }
}
