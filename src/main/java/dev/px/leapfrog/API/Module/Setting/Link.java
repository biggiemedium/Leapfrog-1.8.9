package dev.px.leapfrog.API.Module.Setting;

import dev.px.leapfrog.LeapFrog;

import java.net.MalformedURLException;
import java.net.URL;

public class Link {

    private URL url;

    public Link(String url) {
        setUrlFromString(url);
    }

    public Link(URL url) {
        this.url = url;
    }

    public void setUrlFromString(String urlString) {
        try {
            this.url = new URL(urlString);
        } catch (MalformedURLException e) {
            LeapFrog.LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Invalid URL: " + urlString, e);
        }
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getUrlString() {
        return url.toString();
    }

    public String getDisplayName() {
        if (url == null) {
            return "";
        }
        String host = url.getHost();
        String path = url.getPath();

        // Handle cases where there's no path or just a root path
        if (path == null || path.equals("/")) {
            return host;
        }

        // Shorten the path if it's too long, e.g., only show the first segment
        String[] pathSegments = path.split("/");
        if (pathSegments.length > 1) {
            return host + "/.../" + pathSegments[pathSegments.length - 1];
        } else {
            return host + path;
        }
    }

    public boolean isValid() {
        return url != null;
    }

    @Override
    public String toString() {
        return "Link{" +
                "url=" + url +
                '}';
    }
}
