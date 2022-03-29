package com.dstz.bpm.plugin.util;

import cn.hutool.core.io.IoUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    private static final int BUFFER_SIZE = 2048;

    public FileUtil() {
    }

    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] files = file.list();
                if (files.length > 0) {
                    String[] var3 = files;
                    int var4 = files.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        String childName = var3[var5];
                        deleteFiles(filePath + "/" + childName);
                    }
                }
            }

            file.delete();
        }
    }

    public static void toZipDir(String srcDir, String outPath, boolean KeepDirStructure) throws Exception {
        OutputStream out = new FileOutputStream(outPath);
        ZipOutputStream zos = null;

        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        } catch (Exception var13) {
            throw new RuntimeException("zip error from ZipUtils", var13);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

            out.close();
        }

    }

    public static void toZipFiles(File[] listFiles, OutputStream out, boolean KeepDirStructure) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(out);
        if (listFiles != null && listFiles.length != 0) {
            File[] var4 = listFiles;
            int var5 = listFiles.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (KeepDirStructure) {
                    compress(file, zos, "/" + file.getName(), KeepDirStructure);
                } else {
                    compress(file, zos, file.getName(), KeepDirStructure);
                }
            }
        } else if (KeepDirStructure) {
            zos.putNextEntry(new ZipEntry("/"));
            zos.closeEntry();
        }

    }

    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        ZipFile zip = new ZipFile(zipFile, Charset.forName("UTF-8"));
        Enumeration entries = zip.entries();

        while(true) {
            InputStream in;
            String outPath;
            do {
                if (!entries.hasMoreElements()) {
                    return;
                }

                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);
                outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");
                File file = new File(outPath.substring(0, outPath.lastIndexOf(47)));
                if (!file.exists()) {
                    file.mkdirs();
                }
            } while((new File(outPath)).isDirectory());

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];

            int len;
            while((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }

            in.close();
            out.close();
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[2048];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            FileInputStream in = new FileInputStream(sourceFile);

            int len;
            while((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }

            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles != null && listFiles.length != 0) {
                File[] var11 = listFiles;
                int var7 = listFiles.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    File file = var11[var8];
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            } else if (KeepDirStructure) {
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            }
        }

    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (!file.equals("") && file.getSize() > 0L) {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        } else {
            file = null;
        }

        return toFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];

//            int bytesRead;
            while((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            ins.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static void copyFolder(String resource, String target) throws Exception {
        File resourceFile = new File(resource);
        if (!resourceFile.exists()) {
            throw new Exception("源目标路径：[" + resource + "] 不存在...");
        } else {
            File targetFile = new File(target);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            File[] resourceFiles = resourceFile.listFiles();
            File[] var5 = resourceFiles;
            int var6 = resourceFiles.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                File file = var5[var7];
                File file1 = new File(targetFile.getAbsolutePath() + File.separator + file.getName());
                if (file.isFile()) {
                    InputStream io = new FileInputStream(file);
                    OutputStream out = new FileOutputStream(file1);
                    IoUtil.copy(io, out);
                    out.flush();
                    io.close();
                    out.close();
                }

                if (file.isDirectory()) {
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }

                    copyFolder(file.getAbsolutePath(), file1.getAbsolutePath());
                }
            }

        }
    }

    public static void moveTargetFolderFromNewResource(String oldTarget, String lastResource, String mergeDir) throws Exception {
        File lastResourceFile = new File(lastResource);
        File targetFile = new File(oldTarget);
        if (!targetFile.exists()) {
            throw new Exception("目标路径：[" + oldTarget + "] 不存在...");
        } else if (lastResourceFile.exists()) {
            File mergeFile = new File(mergeDir);
            if (!mergeFile.exists()) {
                mergeFile.mkdirs();
            }

            File[] targetFiles = targetFile.listFiles();
            File[] var7 = targetFiles;
            int var8 = targetFiles.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                File file = var7[var9];
                File newFile = new File(lastResourceFile.getAbsolutePath() + File.separator + file.getName());
                File merge = new File(mergeFile.getAbsoluteFile() + File.separator + file.getName());
                if (file.isFile()) {
                    InputStream io = new FileInputStream(newFile);
                    OutputStream out = new FileOutputStream(merge);
                    IoUtil.copy(io, out);
                    out.flush();
                    io.close();
                    out.close();
                    newFile.delete();
                }

                if (file.isDirectory()) {
                    moveTargetFolderFromNewResource(file.getAbsolutePath(), newFile.getAbsolutePath(), merge.getAbsolutePath());
                    if (newFile.list().length == 0) {
                        newFile.delete();
                    }
                }
            }

        }
    }
}
