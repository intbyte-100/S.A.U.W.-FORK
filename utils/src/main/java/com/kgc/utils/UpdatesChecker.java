package com.kgc.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class UpdatesChecker {
    private static HashMap<String, String> versions = new HashMap<>();

    private static int lastVersionCode;
    private static String androidLink;
    private static String desktopLink;
    private static String changelog = "";
    private static String fullChangelogLink;
    private static Texture screenshot;

    private static final String VERSIONS_LIST_LINK = "https://raw.githubusercontent.com/KirboGames/sauw-db/main/versions";
    private static final String REPOSITIRY_BLOB_LINK = "https://github.com/KirboGames/sauw-db/blob/main/";
    private static final String REPOSITIRY_RAW_LINK = "https://github.com/KirboGames/sauw-db/raw/main/";
    private static final String VERSIONS_RAW_LINK = REPOSITIRY_RAW_LINK + "_versions/";

    private static void getRequest(String url, Net.HttpResponseListener httpResponse) {
        Net.HttpRequest httpRequest = new Net.HttpRequest("GET");
        httpRequest.setUrl(url);
        httpRequest.setContent(null);
        Gdx.net.sendHttpRequest(httpRequest, httpResponse);
    }

    public static void check(UpdatesCallback listener, String lang) {
        getRequest(VERSIONS_LIST_LINK, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String result = httpResponse.getResultAsString();
                versions = new HashMap<>();
                String[] lines = result.split("\\r?\\n");
                for (String line : lines) {
                    String[] keys = line.split("=");
                    versions.put(keys[0], keys[1]);
                }
                lastVersionCode = Integer.parseInt(versions.get("last"));
                String version = versions.get(lastVersionCode + "");
                androidLink = VERSIONS_RAW_LINK + getLastVersionName() + "/android.apk";
                desktopLink = VERSIONS_RAW_LINK + getLastVersionName() + "/desktop.jar";

                fullChangelogLink = REPOSITIRY_BLOB_LINK + "_version/" + version + "/full_changelogs/" + lang + ".md";

                listener.updatesChecked();
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("Error", "failed", t);
            }

            @Override
            public void cancelled() {

            }
        });
    }

    public static void updateChangelog(ChangelogsCallback listener, String lang) {
        getRequest(VERSIONS_RAW_LINK + getLastVersionName() + "/short_changelogs/" + lang + ".txt",
                new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        UpdatesChecker.changelog = httpResponse.getResultAsString();
                        listener.changelogReceived();
                    }

                    @Override
                    public void failed(Throwable t) {
                        Gdx.app.error("Error", "failed", t);
                    }

                    @Override
                    public void cancelled() {
                    }
                });
    }

    public static void updateScreenshot(ScreenshotCallback screenshotCallback) {
        System.out.println(VERSIONS_RAW_LINK + getLastVersionName() + "/img.png");
        getRequest(VERSIONS_RAW_LINK + getLastVersionName() + "/img.png", new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final FileHandle tmpFile = Files.tempFile(getLastVersionName() + ".screenshot.png");
                tmpFile.write(httpResponse.getResultAsStream(), false);
                Gdx.app.postRunnable(() -> {
                    try {
                        if (screenshot != null) {
                            screenshot.dispose();
                            screenshot = null;
                        }
                        screenshot = new Texture(tmpFile);
                        screenshotCallback.screenshotReceived();
                    } catch (Exception e) {
                        Gdx.app.error("error", "Screenshot downloading error", e);
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("Error", "failed", t);
            }

            @Override
            public void cancelled() {

            }
        });
    }

    public static int getLastCodeVersion() {
        return lastVersionCode;
    }

    public static boolean newVersionAvailable(int currentVersion) {
        return lastVersionCode > currentVersion;
    }

    public static String getAndroidLink() {
        return androidLink;
    }

    public static String getDesktopLink() {
        return desktopLink;
    }

    public static String getLastVersionName() {
        return versions.get(versions.get("last"));
    }

    public static String getFullChangelogLink() {
        return fullChangelogLink;
    }

    public static String getChangelog() {
        return changelog;
    }

    public static Texture getScreenshot() {
        return screenshot;
    }

    public static HashMap<String, String> getVersions() {
        return versions;
    }

    public interface UpdatesCallback {
        void updatesChecked();
    }

    public interface ChangelogsCallback {
        void changelogReceived();
    }

    public interface ScreenshotCallback {
        void screenshotReceived();
    }

    public static void disposeScreenshot() {
        if (screenshot != null) screenshot.dispose();
    }
}
