package com.jsp.updaterk312;



public class FTPFileDownloadTask extends Thread {
//    private volatile int err;
//    private volatile long last_times;
//    private long mContentLength;
//    private MyFtpListener mDownloadListener;
//    private FTPRequestInfo mFTPRequest;
//    private FileInfo mFileInfo;
//    private String mFileName;
//    private FTPClient mFtpClient;
//    private volatile long mLastReceivedCount;
//    private String mLocalFilePath;
//    private String mLocalTempFile;
//    private volatile long mReceivedCount;
//
//    public class MyFtpListener implements FTPDataTransferListener {
//        private Handler mProgressHandler;
//        private FTPOptType optType;
//
//        public void setProgressHandler(Handler progressHandler) {
//            this.mProgressHandler = progressHandler;
//        }
//
//        public MyFtpListener(FTPOptType optType) {
//            this.optType = optType;
//        }
//
//        public void started() {
//            if (this.mProgressHandler != null) {
//                Message m = new Message();
//                m.what = 3;
//                this.mProgressHandler.sendMessage(m);
//                Log.d("FTPFileDownloadTask", "send ProgressStartComplete");
//            }
//        }
//
//        public void transferred(int length) {
//            FTPFileDownloadTask.access$014(FTPFileDownloadTask.this, (long) length);
//            long j = (long) length;
//            long posNew = FTPFileDownloadTask.this.mFileInfo.getPieceById(0).getPosNow() + r0;
//            FTPFileDownloadTask.this.mFileInfo.modifyPieceState(0, posNew);
//            FTPFileDownloadTask.this.mFileInfo.setReceivedLength(FTPFileDownloadTask.this.mReceivedCount);
//            long cur_times = System.currentTimeMillis();
//            if (this.mProgressHandler != null) {
//                if (cur_times - FTPFileDownloadTask.this.last_times >= 1000) {
//                    int percent = (int) ((FTPFileDownloadTask.this.mReceivedCount * 100) / FTPFileDownloadTask.this.mContentLength);
//                    long receivedCount = FTPFileDownloadTask.this.mReceivedCount;
//                    long contentLength = FTPFileDownloadTask.this.mContentLength;
//                    long receivedPerSecond = FTPFileDownloadTask.this.mReceivedCount - FTPFileDownloadTask.this.mLastReceivedCount;
//                    FTPFileDownloadTask.this.mLastReceivedCount = FTPFileDownloadTask.this.mReceivedCount;
//                    Message m = new Message();
//                    m.what = 1;
//                    Bundle b = new Bundle();
//                    b.putLong("ContentLength", contentLength);
//                    b.putLong("ReceivedCount", receivedCount);
//                    b.putLong("ReceivedPerSecond", receivedPerSecond);
//                    m.setData(b);
//                    this.mProgressHandler.sendMessage(m);
//                    Log.d("FTPFileDownloadTask", "send ProgressUpdate");
//                    FTPFileDownloadTask.this.last_times = cur_times;
//                }
//            }
//        }
//
//        public void completed() {
//            File f = new File(FTPFileDownloadTask.this.mLocalTempFile);
//            if (f.exists()) {
//                f.delete();
//                Log.d("FTPFileDownloadTask", "finish(): delete the temp file!");
//            }
//            if (this.mProgressHandler != null) {
//                Message m = new Message();
//                m.what = 4;
//                this.mProgressHandler.sendMessage(m);
//                Log.d("FTPFileDownloadTask", "send ProgressDownloadComplete");
//            }
//        }
//
//        public void aborted() {
//            Log.d("FTPFileDownloadTask", "download aborted!!!");
//            File f = new File(FTPFileDownloadTask.this.mLocalTempFile);
//            try {
//                FTPFileDownloadTask.this.mFileInfo.printDebug();
//                RegetInfoUtil.writeFileInfoXml(f, FTPFileDownloadTask.this.mFileInfo);
//                Log.d("FTPFileDownloadTask", "download task not complete, save the progress !!!");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (this.mProgressHandler != null) {
//                Message m = new Message();
//                m.what = 2;
//                Bundle b = new Bundle();
//                b.putInt("err", FTPFileDownloadTask.this.err);
//                m.setData(b);
//                this.mProgressHandler.sendMessage(m);
//                Log.d("FTPFileDownloadTask", "send ProgressStopComplete");
//            }
//            FTPToolkit.closeConnection(FTPFileDownloadTask.this.mFtpClient);
//        }
//
//        public void failed() {
//        }
//    }
//
//    static /* synthetic */ long access$014(FTPFileDownloadTask x0, long x1) {
//        long j = x0.mReceivedCount + x1;
//        x0.mReceivedCount = j;
//        return j;
//    }
//
//    public FTPFileDownloadTask(FTPRequestInfo ftpRequest, String localPath, String fileName) {
//        this.mContentLength = 0;
//        this.last_times = 0;
//        this.err = 0;
//        this.mFTPRequest = ftpRequest;
//        this.mFileName = fileName;
//        this.mDownloadListener = new MyFtpListener(FTPOptType.DOWN);
//        this.mLocalFilePath = (localPath.endsWith("/") ? localPath : localPath + "/") + fileName;
//        StringBuilder stringBuilder = new StringBuilder();
//        if (!localPath.endsWith("/")) {
//            localPath = localPath + "/";
//        }
//        this.mLocalTempFile = stringBuilder.append(localPath).append(".").append(fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) + "__tp.xml" : fileName + "__tp.xml").toString();
//        Log.d("FTPFileDownloadTask", "mLocalFilePath = " + this.mLocalFilePath + "  mLocalTempFile = " + this.mLocalTempFile);
//    }
//
//    public void setProgressHandler(Handler progressHandler) {
//        this.mDownloadListener.setProgressHandler(progressHandler);
//    }
//
//    public void stopDownload() {
//        try {
//            this.err = 3;
//            this.mFtpClient.abortCurrentDataTransfer(true);
//        } catch (Exception e) {
//            this.err = 5;
//            e.printStackTrace();
//        }
//    }
//
//    private void prepareDownload() throws Exception {
//        File targetFile = new File(this.mLocalFilePath);
//        if (targetFile.exists()) {
//            File tmpFile = new File(this.mLocalTempFile);
//            if (tmpFile.exists()) {
//                this.mFileInfo = RegetInfoUtil.parseFileInfoXml(tmpFile);
//                Log.d("FTPFileDownloadTask", "target file have not download complete, so we try to continue download!");
//            } else {
//                targetFile.delete();
//                targetFile.createNewFile();
//                Log.d("FTPFileDownloadTask", "find the same name target file, so delete and rewrite it!!!");
//            }
//        } else {
//            targetFile.createNewFile();
//        }
//        if (this.mFileInfo == null) {
//            this.mFileInfo = new FileInfo();
//            this.mFileInfo.setFileLength(this.mContentLength);
//            this.mFileInfo.setmURI(new URI("ftp://" + this.mFTPRequest.getHost() + this.mFTPRequest.getRequestPath()));
//            this.mFileInfo.setFileName(this.mFileName);
//            this.mFileInfo.setReceivedLength(0);
//        }
//        this.mFtpClient = FTPToolkit.makeFtpConnection(this.mFTPRequest.getHost(), this.mFTPRequest.getPort(), this.mFTPRequest.getUsername(), this.mFTPRequest.getPassword());
//        this.mContentLength = FTPToolkit.getFileLength(this.mFtpClient, this.mFTPRequest.getRequestPath());
//        this.mFileInfo.setFileLength(this.mContentLength);
//    }
//
//    public void run() {
//        try {
//            prepareDownload();
//            if (this.mFileInfo.getPieceNum() == 0) {
//                this.mFileInfo.addPiece(0, this.mContentLength - 1, 0);
//                FTPToolkit.download(this.mFtpClient, this.mFTPRequest.getRequestPath(), this.mLocalFilePath, 0, this.mDownloadListener);
//                return;
//            }
//            Log.d("FTPFileDownloadTask", "try to continue download ====>");
//            this.mReceivedCount = this.mFileInfo.getReceivedLength();
//            FTPToolkit.download(this.mFtpClient, this.mFTPRequest.getRequestPath(), this.mLocalFilePath, this.mFileInfo.getPieceById(0).getPosNow(), this.mDownloadListener);
//        } catch (Exception e) {
//            Log.e("FTPFileDownloadTask", "catch a unknown error!");
//            this.err = 5;
//            this.mDownloadListener.aborted();
//        }
//    }
}
