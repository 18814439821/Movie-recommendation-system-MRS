package com.yolo.mrs.utils;

import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.model.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CoverGet {
    public static Result get(List<Movies> moviesList) {
        for (Movies movies : moviesList) {
            String imageUrl = movies.getCover();
            String localPath = "D:\\java\\idea-project\\Project-MRS\\MRS\\src\\main\\resources\\pic";
            downloadPic(imageUrl, localPath, movies.getMovieId());
        }
        return Result.ok();
    }

    private static void downloadPic(String imageUrl, String localPath, Integer id) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 Edg/128.0.0.0");

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(localPath + id + ".jpg")) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // 重命名文件
            File file = new File(localPath + id + ".jpg");
            if (file.renameTo(new File(localPath + id + ".jpg"))) {
                System.out.println("文件重命名成功");
            } else {
                System.out.println("文件重命名失败");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

