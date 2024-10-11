package com.yolo.mrs.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * 快速生成
 * </p>
 *
 * @author yolo
 * @since 2024-8-29
 */
public class FastAutoGeneratorUtil {

    public static void aaa() {
        String url = "jdbc:mysql://localhost:3306/mrs?serverTimezone=UTC&useUnicode=ture&characterEncoding=utf-8";
        String pkgPath = System.getProperty("user.dir") + "/MRS/src/main/java";
        String pkgXml = System.getProperty("user.dir") + "/MRS/src/main/resources/mapper";
        FastAutoGenerator.create(url, "root", "123456")
                // 全局配置
                .globalConfig((scanner, builder) -> builder.outputDir(pkgPath).author(scanner.apply("请输入作者名称？"))
                        //开启springDoc
                        .enableSpringdoc()
                        //禁止自动打开文件夹
                        .disableOpenDir()
                        )
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？"))
                        .pathInfo(Collections.singletonMap(OutputFile.xml, pkgXml))
                        .entity("model.PO"))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .controllerBuilder().enableRestStyle().enableHyphenStyle()
                        .entityBuilder().enableLombok().addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                                //enableFileOverride开启实体类覆写
                        ).enableFileOverride().build())
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

    public static void main(String[] args) {
        aaa();
    }

}