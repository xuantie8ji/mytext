package com.jsp.updaterk312;

//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.rockchip.update.util.FileInfo;
//import android.rockchip.update.util.FileInfo.Piece;
//import android.rockchip.update.util.RegetInfoUtil;
//import android.util.Log;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.RandomAccessFile;
//import java.net.URI;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import org.apache.http.Header;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;

public class HTTPFileDownloadTask extends Thread {
//    private String TAG;
//    private volatile int err;
//    private boolean mAcceptRanges;
//    private long mContentLength;
//    private boolean mDebug;
//    private ExecutorService mDownloadThreadPool;
//    private FileInfo mFileInfo;
//    private String mFileName;
//    private HttpClient mHttpClient;
//    private volatile long mLastReceivedCount;
//    private String mPath;
//    private int mPoolThreadNum;
//    private Handler mProgressHandler;
//    private volatile long mReceivedCount;
//    private String mTempFileName;
//    private URI mUri;
//    private boolean requestStop;
//    private Object sync;
//
//    private interface DownloadListener {
//        void onErrorOccurre(int i, long j);
//
//        void onPerBlockDown(int i, int i2, long j);
//
//        void onPieceComplete();
//    }
//
//    /* renamed from: android.rockchip.update.service.HTTPFileDownloadTask.1 */
//    class C00031 implements DownloadListener {
//        C00031() {
//        }
//
//        public void onPerBlockDown(int count, int pieceId, long posNew) {
//            synchronized (this) {
//                HTTPFileDownloadTask.access$014(HTTPFileDownloadTask.this, (long) count);
//            }
//            HTTPFileDownloadTask.this.mFileInfo.modifyPieceState(pieceId, posNew);
//            HTTPFileDownloadTask.this.mFileInfo.setReceivedLength(HTTPFileDownloadTask.this.mReceivedCount);
//        }
//
//        public void onPieceComplete() {
//            Log.d(HTTPFileDownloadTask.this.TAG, "one piece complete");
//        }
//
//        public void onErrorOccurre(int pieceId, long posNew) {
//            HTTPFileDownloadTask.this.mFileInfo.modifyPieceState(pieceId, posNew);
//        }
//    }
//
//    private class DownloadFilePieceRunnable implements Runnable {
//        private long mEndPosition;
//        private File mFile;
//        private boolean mIsRange;
//        private DownloadListener mListener;
//        private int mPieceId;
//        private long mPosNow;
//        private long mStartPosition;
//
//        public DownloadFilePieceRunnable(File file, int pieceId, long startPosition, long endPosition, long posNow, boolean isRange) {
//            this.mFile = file;
//            this.mStartPosition = startPosition;
//            this.mEndPosition = endPosition;
//            this.mIsRange = isRange;
//            this.mPieceId = pieceId;
//            this.mPosNow = posNow;
//        }
//
//        public void setDownloadListener(DownloadListener listener) {
//            this.mListener = listener;
//        }
//
//        public void run() {
//            if (HTTPFileDownloadTask.this.mDebug) {
//                Log.d(HTTPFileDownloadTask.this.TAG, "Start:" + this.mStartPosition + "-" + this.mEndPosition + "  posNow:" + this.mPosNow);
//            }
//            try {
//                HttpGet httpGet = new HttpGet(HTTPFileDownloadTask.this.mUri);
//                if (this.mIsRange) {
//                    httpGet.addHeader("Range", "bytes=" + this.mPosNow + "-" + this.mEndPosition);
//                }
//                HttpResponse response = HTTPFileDownloadTask.this.mHttpClient.execute(httpGet);
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (HTTPFileDownloadTask.this.mDebug) {
//                    for (Header header : response.getAllHeaders()) {
//                        Log.d(HTTPFileDownloadTask.this.TAG, header.getName() + ":" + header.getValue());
//                    }
//                    Log.d(HTTPFileDownloadTask.this.TAG, "statusCode:" + statusCode);
//                }
//                if (statusCode == 206 || (statusCode == 200 && !this.mIsRange)) {
//                    InputStream inputStream = response.getEntity().getContent();
//                    RandomAccessFile outputStream = new RandomAccessFile(this.mFile, "rw");
//                    outputStream.seek(this.mPosNow);
//                    byte[] buffer = new byte[4096];
//                    while (true) {
//                        int count = inputStream.read(buffer, 0, buffer.length);
//                        if (count <= 0) {
//                            break;
//                        } else if (Thread.interrupted()) {
//                            Log.d("WorkThread", "interrupted ====>>");
//                            httpGet.abort();
//                            return;
//                        } else {
//                            outputStream.write(buffer, 0, count);
//                            this.mPosNow += (long) count;
//                            afterPerBlockDown(count, this.mPieceId, this.mPosNow);
//                        }
//                    }
//                    outputStream.close();
//                    httpGet.abort();
//                    onePieceComplete();
//                    if (HTTPFileDownloadTask.this.mDebug) {
//                        Log.d(HTTPFileDownloadTask.this.TAG, "End:" + this.mStartPosition + "-" + this.mEndPosition);
//                        return;
//                    }
//                    return;
//                }
//                httpGet.abort();
//                throw new Exception();
//            } catch (IOException e) {
//                ErrorOccurre(this.mPieceId, this.mPosNow);
//                HTTPFileDownloadTask.this.err = 1;
//            } catch (Exception e2) {
//                e2.printStackTrace();
//                ErrorOccurre(this.mPieceId, this.mPosNow);
//                HTTPFileDownloadTask.this.err = 1;
//            }
//        }
//
//        private void afterPerBlockDown(int count, int pieceId, long posNew) {
//            if (this.mListener != null) {
//                this.mListener.onPerBlockDown(count, pieceId, posNew);
//            }
//        }
//
//        private void onePieceComplete() {
//            if (this.mListener != null) {
//                this.mListener.onPieceComplete();
//            }
//        }
//
//        private void ErrorOccurre(int pieceId, long posNew) {
//            if (this.mListener != null) {
//                this.mListener.onErrorOccurre(pieceId, posNew);
//            }
//        }
//    }
//
//    static /* synthetic */ long access$014(HTTPFileDownloadTask x0, long x1) {
//        long j = x0.mReceivedCount + x1;
//        x0.mReceivedCount = j;
//        return j;
//    }
//
//    public HTTPFileDownloadTask(HttpClient httpClient, URI uri, String path, String fileName, int poolThreadNum) {
//        this.TAG = "FileDownloadTask";
//        this.mDebug = true;
//        this.mAcceptRanges = false;
//        this.err = 0;
//        this.requestStop = false;
//        this.sync = new Object();
//        this.mHttpClient = httpClient;
//        this.mPath = path;
//        this.mUri = uri;
//        this.mPoolThreadNum = poolThreadNum;
//        this.mReceivedCount = 0;
//        this.mLastReceivedCount = 0;
//        if (fileName == null) {
//            String uriStr = uri.toString();
//            this.mFileName = uriStr.substring(uriStr.lastIndexOf("/") + 1, uriStr.lastIndexOf("?") > 0 ? uriStr.lastIndexOf("?") : uriStr.length());
//        } else {
//            this.mFileName = fileName;
//        }
//        if (this.mFileName.lastIndexOf(".") > 0) {
//            this.mTempFileName = "." + this.mFileName.substring(0, this.mFileName.lastIndexOf(".")) + "__tp.xml";
//        } else {
//            this.mTempFileName = "." + this.mFileName + "__tp.xml";
//        }
//        Log.d(this.TAG, "tempFileName = " + this.mTempFileName);
//    }
//
//    public void setProgressHandler(Handler progressHandler) {
//        this.mProgressHandler = progressHandler;
//    }
//
//    public void run() {
//        startTask();
//    }
//
//    private void startTask() {
//        try {
//            this.err = 0;
//            this.requestStop = false;
//            getDownloadFileInfo(this.mHttpClient);
//            startWorkThread();
//            monitor();
//            finish();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(this.TAG, "can't connect the network or timeout");
//            this.err = 1;
//            onProgressStopComplete(this.err);
//        } catch (Exception e2) {
//            e2.printStackTrace();
//            onProgressStopComplete(this.err);
//        }
//    }
//
//    public void stopDownload() {
//        this.err = 3;
//        this.requestStop = true;
//    }
//
//    private void onProgressUpdate() {
//        int percent = (int) ((this.mReceivedCount * 100) / this.mContentLength);
//        long receivedCount = this.mReceivedCount;
//        long contentLength = this.mContentLength;
//        long receivedPerSecond = this.mReceivedCount - this.mLastReceivedCount;
//        if (this.mProgressHandler != null) {
//            Message m = new Message();
//            m.what = 1;
//            Bundle b = new Bundle();
//            b.putLong("ContentLength", contentLength);
//            b.putLong("ReceivedCount", receivedCount);
//            b.putLong("ReceivedPerSecond", receivedPerSecond);
//            m.setData(b);
//            this.mProgressHandler.sendMessage(m);
//            Log.d(this.TAG, "send ProgressUpdate");
//        }
//        this.mLastReceivedCount = this.mReceivedCount;
//    }
//
//    private void onProgressStopComplete(int errCode) {
//        if (this.mProgressHandler != null) {
//            Message m = new Message();
//            m.what = 2;
//            Bundle b = new Bundle();
//            b.putInt("err", errCode);
//            m.setData(b);
//            this.mProgressHandler.sendMessage(m);
//            Log.d(this.TAG, "send ProgressStopComplete");
//        }
//    }
//
//    private void onProgressStartComplete() {
//        if (this.mProgressHandler != null) {
//            Message m = new Message();
//            m.what = 3;
//            this.mProgressHandler.sendMessage(m);
//            Log.d(this.TAG, "send ProgressStartComplete");
//        }
//    }
//
//    private void onProgressDownloadComplete() {
//        if (this.mProgressHandler != null) {
//            Message m = new Message();
//            m.what = 4;
//            this.mProgressHandler.sendMessage(m);
//            Log.d(this.TAG, "send ProgressDownloadComplete");
//        }
//    }
//
//    private void finish() throws InterruptedException, IllegalArgumentException, IllegalStateException, IOException {
//        String fullTempfilePath;
//        if (this.err == 0) {
//            if (this.mPath.endsWith("/")) {
//                fullTempfilePath = this.mPath + this.mTempFileName;
//            } else {
//                fullTempfilePath = this.mPath + "/" + this.mTempFileName;
//            }
//            Log.d(this.TAG, "tempfilepath = " + fullTempfilePath);
//            File f = new File(fullTempfilePath);
//            if (f.exists()) {
//                f.delete();
//                Log.d(this.TAG, "finish(): delete the temp file!");
//            }
//            onProgressDownloadComplete();
//            Log.d(this.TAG, "download successfull");
//            return;
//        }
//        if (this.err == 3) {
//            this.mDownloadThreadPool.shutdownNow();
//            while (!this.mDownloadThreadPool.awaitTermination(1, TimeUnit.SECONDS)) {
//                Log.d(this.TAG, "monitor: progress ===== " + this.mReceivedCount + "/" + this.mContentLength);
//                onProgressUpdate();
//            }
//        } else if (this.err == 1) {
//            this.mDownloadThreadPool.shutdown();
//            while (!this.mDownloadThreadPool.awaitTermination(1, TimeUnit.SECONDS) && !this.requestStop) {
//                Log.d(this.TAG, "monitor: progress ===== " + this.mReceivedCount + "/" + this.mContentLength);
//                onProgressUpdate();
//            }
//            this.mDownloadThreadPool.shutdownNow();
//            do {
//            } while (!this.mDownloadThreadPool.awaitTermination(1, TimeUnit.SECONDS));
//        }
//        fullTempfilePath = this.mPath.endsWith("/") ? this.mPath + this.mTempFileName : this.mPath + "/" + this.mTempFileName;
//        Log.d(this.TAG, "tempfilepath = " + fullTempfilePath);
//        RegetInfoUtil.writeFileInfoXml(new File(fullTempfilePath), this.mFileInfo);
//        Log.d(this.TAG, "download task not complete, save the progress !!!");
//        onProgressStopComplete(this.err);
//    }
//
//    private void monitor() {
//        onProgressStartComplete();
//        while (this.mReceivedCount < this.mContentLength && this.err == 0) {
//            Log.d(this.TAG, "monitor: progress ===== " + this.mReceivedCount + "/" + this.mContentLength);
//            try {
//                Thread.sleep(1000);
//                onProgressUpdate();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (this.err == 1) {
//            Log.e(this.TAG, "monitor : ERR_CONNECT_TIMEOUT");
//        }
//        if (this.err == 3) {
//            Log.e(this.TAG, "monitor: ERR_REQUEST_STOP");
//        }
//    }
//
//    private int startWorkThread() throws Exception {
//        String fullPath = this.mPath.endsWith("/") ? this.mPath + this.mFileName : this.mPath + "/" + this.mFileName;
//        String fullTempfilePath = this.mPath.endsWith("/") ? this.mPath + this.mTempFileName : this.mPath + "/" + this.mTempFileName;
//        Log.d(this.TAG, "tempfilepath = " + fullTempfilePath);
//        File targetFile = new File(fullPath);
//        if (targetFile.exists()) {
//            File file = new File(fullTempfilePath);
//            if (file.exists()) {
//                this.mFileInfo = RegetInfoUtil.parseFileInfoXml(file);
//                Log.d(this.TAG, "target file have not download complete, so we try to continue download!");
//            } else {
//                targetFile.delete();
//                targetFile.createNewFile();
//                Log.d(this.TAG, "find the same name target file, so delete and rewrite it!!!");
//            }
//        } else {
//            targetFile.createNewFile();
//        }
//        if (this.mFileInfo == null) {
//            this.mFileInfo = new FileInfo();
//            this.mFileInfo.setFileLength(this.mContentLength);
//            this.mFileInfo.setmURI(this.mUri);
//            this.mFileInfo.setFileName(this.mFileName);
//            this.mFileInfo.setReceivedLength(0);
//        }
//        if (this.mFileInfo.getFileLength() == this.mContentLength || !this.mFileInfo.getURI().equals(this.mUri)) {
//            DownloadListener c00031 = new C00031();
//            Piece p;
//            if (this.mAcceptRanges) {
//                Log.d(this.TAG, "Support Ranges");
//                if (this.mDownloadThreadPool == null) {
//                    this.mDownloadThreadPool = Executors.newFixedThreadPool(this.mPoolThreadNum);
//                }
//                if (this.mFileInfo.getPieceNum() == 0) {
//                    long pieceSize = (this.mContentLength / ((long) this.mPoolThreadNum)) + 1;
//                    long start = 0;
//                    long end = pieceSize - 1;
//                    int pieceId = 0;
//                    do {
//                        if (end > this.mContentLength - 1) {
//                            end = this.mContentLength - 1;
//                        }
//                        Log.d(this.TAG, "piece info, startpos = " + start + " , endpos = " + end);
//                        DownloadFilePieceRunnable task = new DownloadFilePieceRunnable(targetFile, pieceId, start, end, start, true);
//                        this.mFileInfo.addPiece(start, end, start);
//                        task.setDownloadListener(c00031);
//                        this.mDownloadThreadPool.execute(task);
//                        start += pieceSize;
//                        end = (start + pieceSize) - 1;
//                        pieceId++;
//                    } while (start < this.mContentLength);
//                } else {
//                    Log.d(this.TAG, "try to continue download ====>");
//                    this.mReceivedCount = this.mFileInfo.getReceivedLength();
//                    for (int index = 0; index < this.mFileInfo.getPieceNum(); index++) {
//                        p = this.mFileInfo.getPieceById(index);
//                        DownloadFilePieceRunnable downloadFilePieceRunnable = new DownloadFilePieceRunnable(targetFile, index, p.getStart(), p.getEnd(), p.getPosNow(), true);
//                        downloadFilePieceRunnable.setDownloadListener(c00031);
//                        this.mDownloadThreadPool.execute(downloadFilePieceRunnable);
//                    }
//                }
//            } else {
//                Log.d(this.TAG, "Can't Ranges!");
//                if (this.mDownloadThreadPool == null) {
//                    this.mDownloadThreadPool = Executors.newFixedThreadPool(1);
//                }
//                DownloadFilePieceRunnable downloadFilePieceRunnable2;
//                if (this.mFileInfo.getPieceNum() == 0) {
//                    downloadFilePieceRunnable2 = new DownloadFilePieceRunnable(targetFile, 0, 0, this.mContentLength - 1, 0, false);
//                    this.mFileInfo.addPiece(0, this.mContentLength - 1, 0);
//                    downloadFilePieceRunnable2.setDownloadListener(c00031);
//                    this.mDownloadThreadPool.execute(downloadFilePieceRunnable2);
//                } else {
//                    Log.d(this.TAG, "try to continue download ====>");
//                    this.mReceivedCount = 0;
//                    p = this.mFileInfo.getPieceById(0);
//                    p.setPosNow(0);
//                    downloadFilePieceRunnable2 = new DownloadFilePieceRunnable(targetFile, 0, 0, this.mContentLength - 1, p.getPosNow(), false);
//                    downloadFilePieceRunnable2.setDownloadListener(c00031);
//                    this.mDownloadThreadPool.execute(downloadFilePieceRunnable2);
//                }
//            }
//            return 0;
//        }
//        this.err = 2;
//        Log.e(this.TAG, "FileLength or uri not the same, you can't continue download!");
//        throw new Exception("ERR_FILELENGTH_NOMATCH!");
//    }
//
//    private void getDownloadFileInfo(HttpClient httpClient) throws IOException, ClientProtocolException, Exception {
//        HttpGet httpGet = new HttpGet(this.mUri);
//        HttpResponse response = httpClient.execute(httpGet);
//        int statusCode = response.getStatusLine().getStatusCode();
//        if (statusCode != 200) {
//            this.err = 4;
//            Log.d(this.TAG, "response statusCode = " + statusCode);
//            throw new Exception("resource is not exist!");
//        }
//        if (this.mDebug) {
//            for (Header header : response.getAllHeaders()) {
//                Log.d(this.TAG, header.getName() + ":" + header.getValue());
//            }
//        }
//        Header[] headers = response.getHeaders("Content-Length");
//        if (headers.length > 0) {
//            this.mContentLength = Long.valueOf(headers[0].getValue()).longValue();
//        }
//        httpGet.abort();
//        httpGet = new HttpGet(this.mUri);
//        httpGet.addHeader("Range", "bytes=0-" + (this.mContentLength - 1));
//        if (httpClient.execute(httpGet).getStatusLine().getStatusCode() == 206) {
//            this.mAcceptRanges = true;
//        }
//        httpGet.abort();
//    }
}
