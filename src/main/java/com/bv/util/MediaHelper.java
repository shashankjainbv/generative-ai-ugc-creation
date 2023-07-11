package com.bv.util;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utility methods for photo / video submissions.
 */
public class MediaHelper {

    /**
     * Parse the photo storage location (s3 or DB) from the request url
     *
     * @param photoUrl url to parse
     * @return {storage host} (s3 or 'bazaarvoice') + '_' + {host specific id}
     */
    public static String getPhotoLocationStringFromUrl(String photoUrl) {
        try {
            String urlPath = new URL(photoUrl).getPath();
            String[] pathParts = StringUtils.split(urlPath, '/');
            Assert.assertTrue(pathParts.length > 2);
            String fileName = pathParts[pathParts.length - 1];
            Assert.assertTrue(fileName.startsWith("photo"));
            Assert.assertTrue(fileName.endsWith(".jpg") || fileName.endsWith(".png"));
            String photoLocation = pathParts[pathParts.length - 2];
            String locationWithoutSize = StringUtils.substringBefore(photoLocation, "_imgsz");
            if (photoLocation.equals(locationWithoutSize)) {
                return photoLocation;
            }
            String idStr = StringUtils.substringAfterLast(photoLocation, "_");
            return locationWithoutSize + "_" + idStr;
        } catch (MalformedURLException ex) {
            Assert.fail("Malformed test url: " + photoUrl);
            return "";
        }
    }

    /**
     * For you tube videos, we parse the you tuve ID from the url. This can be compared to
     * the 'VideoID' column of the video entry in the database.
     *
     * For hosted videos, the URL returned from upload is no help because the s3 location
     * in the URL is converted to a brightcove ID immediately upon reciept at CMS. Instead
     * we need use the video hash for the '28.mp4' video that is always used by the test
     * suite. Ideally we would vary the video file, and calculate the hash in the test
     * suite for comparison with the DB.
     *
     * @param videoUrl url to parse
     * @return the host name concetenated with '==' and either the video ID or hash
     */
    public static String getVideoInfo(String videoUrl) {
        try {
            URL urlasUrl = new URL(videoUrl);
            if (videoUrl.contains("youtube")) {
                String urlQuery = urlasUrl.getQuery();
                String[] queryParts = StringUtils.split(urlQuery, '=');
                Assert.assertTrue(queryParts.length == 2);
                Assert.assertEquals(queryParts[0], "v");
                return queryParts[1];
            }
            return "71e402f934677cc5ae6692ad6a3ea871";
        } catch (MalformedURLException ex) {
            Assert.fail("Malformed test url: " + videoUrl);
            return "";
        }
    }

}
