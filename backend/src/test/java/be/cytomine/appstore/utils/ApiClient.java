package be.cytomine.appstore.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import be.cytomine.appstore.dto.inputs.task.TaskDescription;
import be.cytomine.appstore.dto.inputs.task.TaskInput;
import be.cytomine.appstore.dto.inputs.task.TaskOutput;
import be.cytomine.appstore.models.task.Parameter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ApiClient {

    private final RestTemplate restTemplate;

    private String baseUrl;

    private String port;

    public ApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    public <T> ResponseEntity<Resource> getData(String url, HttpEntity<Object> entity, Map<String,String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        String finalUrl = builder.toUriString();

        return restTemplate.exchange(
            finalUrl,
            HttpMethod.GET,
            entity,
            Resource.class
        );

    }

    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> post(String url, Object body, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), responseType);
    }

    public <T> ResponseEntity<T> postJson(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    public <T> ResponseEntity<T> postData(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    public <T> ResponseEntity<T> put(String url, HttpEntity<Object> entity, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
    }

    public <T> ResponseEntity<T> put(String url, HttpEntity<Object> entity, Class<T> responseType, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        String finalUrl = builder.toUriString();

        return restTemplate.exchange(finalUrl, HttpMethod.PUT, entity, responseType);
    }

    public <T> ResponseEntity<T> put(String url, HttpEntity<Object> entity, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
    }

    public TaskDescription uploadTask(File task) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("task", new FileSystemResource(task));

        return postData(baseUrl + "/schemas/tasks", body, TaskDescription.class).getBody();
    }

    public TaskDescription getTask(String namespace, String version) {
        return get(baseUrl + "/schemas/tasks/" + namespace + "/" + version, TaskDescription.class).getBody();
    }

    public TaskDescription getTask(String uuid) {
        return get(baseUrl + "/schemas/tasks/" + uuid, TaskDescription.class).getBody();
    }

    public List<TaskDescription> getTasks() {
        return get(baseUrl + "/schemas/tasks", new ParameterizedTypeReference<List<TaskDescription>>() {}).getBody();
    }

    public File getTaskDescriptor(String namespace, String version) {
        String url = baseUrl + "/schemas/tasks/" + namespace + "/" + version + "/descriptor.yml";
        byte[] resource = get(url, byte[].class).getBody();

        File data = FileHelper.write("descriptor-", resource);
        data.deleteOnExit();

        return data;
    }

    public File getTaskDescriptor(String uuid) {
        String url = baseUrl + "/schemas/tasks/" + uuid + "/descriptor.yml";
        byte[] resource = get(url, byte[].class).getBody();

        File data = FileHelper.write("descriptor-", resource);
        data.deleteOnExit();

        return data;
    }

    public List<TaskInput> getTaskInputs(String namespace, String version) {
        String url = baseUrl + "/schemas/tasks/" + namespace + "/" + version + "/inputs";
        return get(url, new ParameterizedTypeReference<List<TaskInput>>() {}).getBody();
    }

    public List<TaskInput> getTaskInputs(String uuid) {
        String url = baseUrl + "/schemas/tasks/" + uuid + "/inputs";
        return get(url, new ParameterizedTypeReference<List<TaskInput>>() {}).getBody();
    }

    public List<TaskOutput> getTaskOutputs(String namespace, String version) {
        String url = baseUrl + "/schemas/tasks/" + namespace + "/" + version + "/outputs";
        return get(url, new ParameterizedTypeReference<List<TaskOutput>>() {}).getBody();
    }

    public List<TaskOutput> getTaskOutputs(String uuid) {
        String url = baseUrl + "/schemas/tasks/" + uuid + "/outputs";
        return get(url, new ParameterizedTypeReference<List<TaskOutput>>() {}).getBody();
    }

    public List<Parameter> getInputs(String namespace, String version) {
        String url = baseUrl + "/schemas/tasks/" + namespace + "/" + version + "/inputs";
        return get(url, new ParameterizedTypeReference<List<Parameter>>() {}).getBody();
    }

    public List<Parameter> getInputs(String uuid) {
        String url = baseUrl + "/schemas/tasks/" + uuid + "/inputs";
        return get(url, new ParameterizedTypeReference<List<Parameter>>() {}).getBody();
    }

    public List<Parameter> getOutputs(String namespace, String version) {
        String url = baseUrl + "/schemas/tasks/" + namespace + "/" + version + "/outputs";
        return get(url, new ParameterizedTypeReference<List<Parameter>>() {}).getBody();
    }

    public List<Parameter> getOutputs(String uuid) {
        String url = baseUrl + "/schemas/tasks/" + uuid + "/outputs";
        return get(url, new ParameterizedTypeReference<List<Parameter>>() {}).getBody();
    }
}
