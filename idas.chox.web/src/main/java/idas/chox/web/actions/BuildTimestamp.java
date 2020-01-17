package idas.chox.web.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class BuildTimestamp extends ActionSupport {
    @Value("${build.timestamp}")
    private String buildTimestamp;

    public String getJsonData() throws IOException {
        if (null == buildTimestamp) {
            Resource resource = new ClassPathResource("/build.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            buildTimestamp = props.getProperty("build.timestamp", "");
        }

        return String.format("{\"buildTimestamp\":\"%s\"}", buildTimestamp);
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}
