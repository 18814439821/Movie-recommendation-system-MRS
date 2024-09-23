package com.yolo.mrs.utils;

import com.yolo.mrs.mapper.MoviesMapper;
import com.yolo.mrs.model.PO.MovieMid;
import com.yolo.mrs.model.PO.Movies;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IMoviesService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieData {


    public static String AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 Edg/128.0.0.0";

    public static void MovieDataReady(List<Movies> moviesList, List<MovieMid> movieMidList) {
        //请求
        HttpClient httpClient = HttpClient.newHttpClient();
//        //创建了一个ClassPathXmlApplicationContext对象，用于加载applicationContext.xml配置文件
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        //从Spring容器中获取IMoviesService接口的实现类对象。
//        IMoviesService moviesService = context.getBean(IMoviesService.class);
        try {
            int i = 0;
            while(i < 251){
                HttpRequest build = HttpRequest.newBuilder()
                        .uri(URI.create("https://movie.douban.com/top250?start="+ i +"&filter="))
                        .header("User-Agent", AGENT)
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
                //解析信息
                Document doc = Jsoup.parse(response.body());
                getInfo(doc, moviesList,movieMidList);
                i += 25;
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getInfo(Document doc, List<Movies> moviesList, List<MovieMid> movieMidList) {
        Elements divDivH1 = doc.select("div div div div ol li");
        if (!divDivH1.isEmpty()){
            //li中包含多个电影信息，遍历这些li
            for (Element element : divDivH1) {
                //单个电影信息
                String movieName = Jsoup.parse(element.toString()).select("div div div a span").first().text();
                System.out.print("movieName = " + movieName + " ");
                String star = Jsoup.parse(element.toString()).select("div div div.bd div span.rating_num").text();
                System.out.println("star = " + star);
                String cover = Jsoup.parse(element.toString()).select("div div a img").attr("src");
                System.out.println("cover = " + cover);
                String witticism = Jsoup.parse(element.toString()).select("div div div p.quote").text();
                System.out.println("witticism = " + witticism);
                //获取日期，这里日期是在另一个网址，所以我们这么要获取跳转的网址
                String movieUrl = Jsoup.parse(element.toString()).select("div div div a")
                        .attr("href");
                String releaseDate = "";
                        //通过这个url去获取上映日期，类型信息
                HttpRequest build = HttpRequest.newBuilder()
                        .uri(URI.create(movieUrl))
                        .header("User-Agent", AGENT)
                        .GET()
                        .build();
                HttpClient httpClient = HttpClient.newHttpClient();
                try {
                    HttpResponse<String> body = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
                    //获取电影详细页面源码
                    Document movieDetail = Jsoup.parse(body.body());
                    String movieGenre = Jsoup.parse(movieDetail.toString()).select("#wrapper div div.clearfix div div div div #info span[property=v:genre]").text();
                    String initialReleaseDate = movieDetail.select("#wrapper div div.clearfix div div div div #info span[property=v:initialReleaseDate]").text();
                    //处理电影类别信息
                    if (!movieGenre.isEmpty()) {
                        //如果类别串不为空，就把他写入MovieMidList
                        MovieMid movieMid = new MovieMid();
                        movieMid.setGenre(movieGenre);
                        movieMid.setName(movieName);
                        movieMidList.add(movieMid);
                    }
                    //处理电影上映日期
                    releaseDate = extractYearAndLastSegment(initialReleaseDate);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy");
                Movies movie = new Movies();
                movie.setMovieName(movieName);
                movie.setStars(star);
                movie.setCover(cover);
                movie.setWitticism(witticism);
                try {
                    //两种匹配格式，如果是单年份没有日期就匹配else，如果有准确的日期就匹配if
                    if (releaseDate.length() > 5) {
                        Date parse = dateFormat1.parse(releaseDate);
                        movie.setReleaseDate(parse);
                    }else {
                        Date parse = dateFormat2.parse(releaseDate);
                        movie.setReleaseDate(parse);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                moviesList.add(movie);
            }
        }
    }

    public static String extractYearAndLastSegment(String input) {
        // 确保输入字符串不为null或空
        if (input == null || input.isEmpty()) {
            return null;
        }
        System.out.println("input = " + input);
        // 用于存储年份
        String year = null;

        // 查找年份（四位数字）
        Pattern yearPattern1 = Pattern.compile("\\b\\d{4}-\\d{2}-\\d{2}\\b");
        Pattern yearPattern2 = Pattern.compile("\\b\\d{4}\\b");
        Matcher yearMatcher1 = yearPattern1.matcher(input);
        Matcher yearMatcher2 = yearPattern2.matcher(input);
        if (yearMatcher1.find()) {
            year = yearMatcher1.group(); // 提取年份
        }else if (yearMatcher2.find()){
            year = yearMatcher2.group();
        }

        // 如果没有找到年份，直接返回null或原始输入（取决于你的需求）
        if (year == null) {
            return "";
        }
        // 查找最后一个'/'的位置
        int lastIndex = input.lastIndexOf('/');

        // 确保'/'在年份之后
        if (lastIndex < input.indexOf(year)) {
            // 如果没有在年份之后找到'/'，或者字符串以年份结尾，则可能没有后续内容
            // 根据你的需求，这里可以返回null、年份或原始输入的一部分
            return year; // 或者其他你认为合适的值
        }
        return year;
        // 提取最后一个'/'之后的内容
//        String afterLastSlash = input.substring(lastIndex + 1).trim();

        // 如果需要，可以返回年份和最后一个'/'之后的内容的组合
        // 但基于你的需求，这里只返回最后一个'/'之后的内容
//        return afterLastSlash;

        // 如果你确实需要年份和后续内容的组合（尽管你的描述只要求后者），可以这样做：
        // return year + " / " + afterLastSlash;
    }

    //下载封面
    public static Result get(List<Movies> moviesList) {
        for (Movies movies : moviesList) {
            String imageUrl = movies.getCover();
            String localPath = "D:\\java\\idea-project\\Project-MRS\\MRS\\src\\main\\resources\\pic\\";
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