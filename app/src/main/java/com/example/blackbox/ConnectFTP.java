package com.example.blackbox;

import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class ConnectFTP {
    private final String TAG = "Connect FTP";
    // "Connect FTP" TAG 라는 이름의 스트링 상수로 지정
    public FTPClient mFTPClient = null;
    // FTPClient타입의 변수 mFTPClient를 생성

    public ConnectFTP(){
        // ConnectFTP()의 생성자
        mFTPClient = new FTPClient();
    }

    public String ftpConnect(String host, String username, String password, int port) {
        boolean check = false;
        try{
            mFTPClient.connect(host, port);

            if(FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                check = mFTPClient.login(username, password);
                mFTPClient.enterLocalPassiveMode();
            }
        }catch (Exception e){
            Log.d(TAG, "Couldn't connect to host");
        }

        if(check)
        {
            return new String("FTP 연결 성공");
        }else{
            return new String("FTP 연결 실패");
        }

    }
    // FTP 서버와 연결

    public boolean ftpChangeDirectory(String directory) {
        try{
            mFTPClient.changeWorkingDirectory(directory);
            return true;
        }catch (Exception e){
            Log.d(TAG, "Couldn't change the directory");
        }
        return false;
    }
    // 작업 경로 수정

    public String[] ftpGetFileList(String directory) {
        String[] fileList = null;
        int i = 0;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(directory);
            fileList = new String[ftpFiles.length];

            for(FTPFile file : ftpFiles) {
                String fileName = file.getName();
                fileList[i] = fileName;
                i++;
            }


        } catch (Exception e) {
            fileList= new String[]{"ftpGetFileList 실패", "실패야"};
            e.printStackTrace();
        }
        return fileList;
    }
    // directory 내 파일 리스트 가져오기

    public boolean ftpDownloadFile(String srcFilePath, String desFilePath) {
        boolean result = false;
        try{
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            FileOutputStream fos = new FileOutputStream(desFilePath);
            result = mFTPClient.retrieveFile(srcFilePath, fos);
            fos.close();
        } catch (Exception e){
            Log.d(TAG, "Download failed");
        }
        return result;
    }
    // 파일 다운로드

    public String ftpUploadFile(String srcFilePath, String desFileName, String desDirectory) {
        boolean check = false;
        try {
            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
            mFTPClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            FileInputStream fis = new FileInputStream(srcFilePath);
            if(ftpChangeDirectory(desDirectory)) {
                check = mFTPClient.storeFile(desFileName, fis);
            }
            fis.close();
        } catch(Exception e){
            Log.d(TAG, "Couldn't upload the file");
        }
        if(check)
        {
            return new String("파일 업로드 성공");
        }else{
            return new String("파일 업로드 실패");
        }
    }
    // FTP 서버에 파일 업로드
}
