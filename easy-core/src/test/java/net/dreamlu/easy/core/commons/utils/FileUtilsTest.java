package net.dreamlu.easy.core.commons.utils;

import java.io.File;
import java.util.List;

import net.dreamlu.easy.commons.utils.FileUtils;

public class FileUtilsTest {

    public static void main(String[] args) {
        List<File> xx = FileUtils.list("C://github");
        System.out.println(xx);
    }
}
