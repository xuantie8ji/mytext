package com.jsp.updaterk312;

import android.content.Context;
import android.os.PowerManager;

import android.util.Log;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class RecoverySystem {
    private static File COMMAND_FILE;
    private static final File DEFAULT_KEYSTORE;
    private static String LAST_PREFIX;
    private static File LOG_FILE;
    private static int LOG_FILE_MAX_LENGTH;
    private static File RECOVERY_DIR;
    private static File UPDATE_FLAG_FILE;

    public interface ProgressListener {
        void onProgress(int i);
    }

    static {
        DEFAULT_KEYSTORE = new File("/system/etc/security/otacerts.zip");
        RECOVERY_DIR = new File("/cache/recovery");
        UPDATE_FLAG_FILE = new File(RECOVERY_DIR, "last_flag");
        COMMAND_FILE = new File(RECOVERY_DIR, "command");
        LOG_FILE = new File(RECOVERY_DIR, "log");
        LAST_PREFIX = "last_";
        LOG_FILE_MAX_LENGTH = 65536;
    }

    private static HashSet<X509Certificate> getTrustedCerts(File keystore) throws IOException, GeneralSecurityException {
        InputStream is;
        HashSet<X509Certificate> trusted = new HashSet();
        if (keystore == null) {
            keystore = DEFAULT_KEYSTORE;
        }
        ZipFile zip = new ZipFile(keystore);
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                is = zip.getInputStream((ZipEntry) entries.nextElement());
                trusted.add((X509Certificate) cf.generateCertificate(is));
                is.close();
            }
            zip.close();
            return trusted;
        } catch (Throwable th) {
            zip.close();
        }
return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void verifyPackage(File r50, com.jsp.updaterk312.RecoverySystem.ProgressListener r51, File r52) throws IOException, GeneralSecurityException {
        /*
        r16 = r50.length();
        r30 = new java.io.RandomAccessFile;
        r45 = "r";
        r0 = r30;
        r1 = r50;
        r2 = r45;
        r0.<init>(r1, r2);
        r24 = 0;
        r26 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0056 }
        if (r51 == 0) goto L_0x0020;
    L_0x0019:
        r0 = r51;
        r1 = r24;
        r0.onProgress(r1);	 Catch:{ all -> 0x0056 }
    L_0x0020:
        r46 = 6;
        r46 = r16 - r46;
        r0 = r30;
        r1 = r46;
        r0.seek(r1);	 Catch:{ all -> 0x0056 }
        r45 = 6;
        r0 = r45;
        r15 = new byte[r0];	 Catch:{ all -> 0x0056 }
        r0 = r30;
        r0.readFully(r15);	 Catch:{ all -> 0x0056 }
        r45 = 2;
        r45 = r15[r45];	 Catch:{ all -> 0x0056 }
        r46 = -1;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x004e;
    L_0x0042:
        r45 = 3;
        r45 = r15[r45];	 Catch:{ all -> 0x0056 }
        r46 = -1;
        r0 = r45;
        r1 = r46;
        if (r0 == r1) goto L_0x005b;
    L_0x004e:
        r45 = new java.security.SignatureException;	 Catch:{ all -> 0x0056 }
        r46 = "no signature in file (no footer)";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x0056:
        r45 = move-exception;
        r30.close();
        throw r45;
    L_0x005b:
        r45 = 4;
        r45 = r15[r45];	 Catch:{ all -> 0x0056 }
        r0 = r45;
        r0 = r0 & 255;
        r45 = r0;
        r46 = 5;
        r46 = r15[r46];	 Catch:{ all -> 0x0056 }
        r0 = r46;
        r0 = r0 & 255;
        r46 = r0;
        r46 = r46 << 8;
        r10 = r45 | r46;
        r45 = 0;
        r45 = r15[r45];	 Catch:{ all -> 0x0056 }
        r0 = r45;
        r0 = r0 & 255;
        r45 = r0;
        r46 = 1;
        r46 = r15[r46];	 Catch:{ all -> 0x0056 }
        r0 = r46;
        r0 = r0 & 255;
        r46 = r0;
        r46 = r46 << 8;
        r36 = r45 | r46;
        r45 = r10 + 22;
        r0 = r45;
        r14 = new byte[r0];	 Catch:{ all -> 0x0056 }
        r45 = r10 + 22;
        r0 = r45;
        r0 = (long) r0;	 Catch:{ all -> 0x0056 }
        r46 = r0;
        r46 = r16 - r46;
        r0 = r30;
        r1 = r46;
        r0.seek(r1);	 Catch:{ all -> 0x0056 }
        r0 = r30;
        r0.readFully(r14);	 Catch:{ all -> 0x0056 }
        r45 = 0;
        r45 = r14[r45];	 Catch:{ all -> 0x0056 }
        r46 = 80;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x00d6;
    L_0x00b2:
        r45 = 1;
        r45 = r14[r45];	 Catch:{ all -> 0x0056 }
        r46 = 75;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x00d6;
    L_0x00be:
        r45 = 2;
        r45 = r14[r45];	 Catch:{ all -> 0x0056 }
        r46 = 5;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x00d6;
    L_0x00ca:
        r45 = 3;
        r45 = r14[r45];	 Catch:{ all -> 0x0056 }
        r46 = 6;
        r0 = r45;
        r1 = r46;
        if (r0 == r1) goto L_0x00de;
    L_0x00d6:
        r45 = new java.security.SignatureException;	 Catch:{ all -> 0x0056 }
        r46 = "no signature in file (bad footer)";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x00de:
        r18 = 4;
    L_0x00e0:
        r0 = r14.length;	 Catch:{ all -> 0x0056 }
        r45 = r0;
        r45 = r45 + -3;
        r0 = r18;
        r1 = r45;
        if (r0 >= r1) goto L_0x0124;
    L_0x00eb:
        r45 = r14[r18];	 Catch:{ all -> 0x0056 }
        r46 = 80;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x0121;
    L_0x00f5:
        r45 = r18 + 1;
        r45 = r14[r45];	 Catch:{ all -> 0x0056 }
        r46 = 75;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x0121;
    L_0x0101:
        r45 = r18 + 2;
        r45 = r14[r45];	 Catch:{ all -> 0x0056 }
        r46 = 5;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x0121;
    L_0x010d:
        r45 = r18 + 3;
        r45 = r14[r45];	 Catch:{ all -> 0x0056 }
        r46 = 6;
        r0 = r45;
        r1 = r46;
        if (r0 != r1) goto L_0x0121;
    L_0x0119:
        r45 = new java.security.SignatureException;	 Catch:{ all -> 0x0056 }
        r46 = "EOCD marker found after start of EOCD";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x0121:
        r18 = r18 + 1;
        goto L_0x00e0;
    L_0x0124:
        r5 = new org.apache.harmony.security.asn1.BerInputStream;	 Catch:{ all -> 0x0056 }
        r45 = new java.io.ByteArrayInputStream;	 Catch:{ all -> 0x0056 }
        r46 = r10 + 22;
        r46 = r46 - r36;
        r0 = r45;
        r1 = r46;
        r2 = r36;
        r0.<init>(r14, r1, r2);	 Catch:{ all -> 0x0056 }
        r0 = r45;
        r5.<init>(r0);	 Catch:{ all -> 0x0056 }
        r45 = org.apache.harmony.security.pkcs7.ContentInfo.ASN1;	 Catch:{ all -> 0x0056 }
        r0 = r45;
        r20 = r0.decode(r5);	 Catch:{ all -> 0x0056 }
        r20 = (org.apache.harmony.security.pkcs7.ContentInfo) r20;	 Catch:{ all -> 0x0056 }
        r37 = r20.getSignedData();	 Catch:{ all -> 0x0056 }
        if (r37 != 0) goto L_0x0152;
    L_0x014a:
        r45 = new java.io.IOException;	 Catch:{ all -> 0x0056 }
        r46 = "signedData is null";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x0152:
        r13 = r37.getCertificates();	 Catch:{ all -> 0x0056 }
        r45 = r13.isEmpty();	 Catch:{ all -> 0x0056 }
        if (r45 == 0) goto L_0x0164;
    L_0x015c:
        r45 = new java.io.IOException;	 Catch:{ all -> 0x0056 }
        r46 = "encCerts is empty";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x0164:
        r23 = r13.iterator();	 Catch:{ all -> 0x0056 }
        r8 = 0;
        r45 = r23.hasNext();	 Catch:{ all -> 0x0056 }
        if (r45 == 0) goto L_0x01de;
    L_0x016f:
        r45 = "X.509";
        r9 = java.security.cert.CertificateFactory.getInstance(r45);	 Catch:{ all -> 0x0056 }
        r22 = new java.io.ByteArrayInputStream;	 Catch:{ all -> 0x0056 }
        r45 = r23.next();	 Catch:{ all -> 0x0056 }
        r45 = (org.apache.harmony.security.x509.Certificate) r45;	 Catch:{ all -> 0x0056 }
        r45 = r45.getEncoded();	 Catch:{ all -> 0x0056 }
        r0 = r22;
        r1 = r45;
        r0.<init>(r1);	 Catch:{ all -> 0x0056 }
        r0 = r22;
        r8 = r9.generateCertificate(r0);	 Catch:{ all -> 0x0056 }
        r8 = (java.security.cert.X509Certificate) r8;	 Catch:{ all -> 0x0056 }
        r34 = r37.getSignerInfos();	 Catch:{ all -> 0x0056 }
        r45 = r34.isEmpty();	 Catch:{ all -> 0x0056 }
        if (r45 != 0) goto L_0x01e6;
    L_0x019a:
        r45 = 0;
        r0 = r34;
        r1 = r45;
        r33 = r0.get(r1);	 Catch:{ all -> 0x0056 }
        r33 = (org.apache.harmony.security.pkcs7.SignerInfo) r33;	 Catch:{ all -> 0x0056 }
        if (r52 != 0) goto L_0x01aa;
    L_0x01a8:
        r52 = DEFAULT_KEYSTORE;	 Catch:{ all -> 0x0056 }
    L_0x01aa:
        r39 = getTrustedCerts(r52);	 Catch:{ all -> 0x0056 }
        r35 = r8.getPublicKey();	 Catch:{ all -> 0x0056 }
        r44 = 0;
        r19 = r39.iterator();	 Catch:{ all -> 0x0056 }
    L_0x01b8:
        r45 = r19.hasNext();	 Catch:{ all -> 0x0056 }
        if (r45 == 0) goto L_0x01d4;
    L_0x01be:
        r7 = r19.next();	 Catch:{ all -> 0x0056 }
        r7 = (java.security.cert.X509Certificate) r7;	 Catch:{ all -> 0x0056 }
        r45 = r7.getPublicKey();	 Catch:{ all -> 0x0056 }
        r0 = r45;
        r1 = r35;
        r45 = r0.equals(r1);	 Catch:{ all -> 0x0056 }
        if (r45 == 0) goto L_0x01b8;
    L_0x01d2:
        r44 = 1;
    L_0x01d4:
        if (r44 != 0) goto L_0x01ee;
    L_0x01d6:
        r45 = new java.security.SignatureException;	 Catch:{ all -> 0x0056 }
        r46 = "signature doesn't match any trusted key";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x01de:
        r45 = new java.security.SignatureException;	 Catch:{ all -> 0x0056 }
        r46 = "signature contains no certificates";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x01e6:
        r45 = new java.io.IOException;	 Catch:{ all -> 0x0056 }
        r46 = "no signer infos!";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x01ee:
        r11 = r33.getDigestAlgorithm();	 Catch:{ all -> 0x0056 }
        r12 = r33.getDigestEncryptionAlgorithm();	 Catch:{ all -> 0x0056 }
        r4 = 0;
        if (r11 == 0) goto L_0x01fb;
    L_0x01f9:
        if (r12 != 0) goto L_0x0243;
    L_0x01fb:
        r4 = r8.getSigAlgName();	 Catch:{ all -> 0x0056 }
    L_0x01ff:
        r32 = java.security.Signature.getInstance(r4);	 Catch:{ all -> 0x0056 }
        r0 = r32;
        r0.initVerify(r8);	 Catch:{ all -> 0x0056 }
        r0 = (long) r10;	 Catch:{ all -> 0x0056 }
        r46 = r0;
        r46 = r16 - r46;
        r48 = 2;
        r42 = r46 - r48;
        r40 = 0;
        r46 = 0;
        r0 = r30;
        r1 = r46;
        r0.seek(r1);	 Catch:{ all -> 0x0056 }
        r45 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r45;
        r6 = new byte[r0];	 Catch:{ all -> 0x0056 }
        r21 = 0;
    L_0x0224:
        r45 = (r40 > r42 ? 1 : (r40 == r42 ? 0 : -1));
        if (r45 >= 0) goto L_0x022e;
    L_0x0228:
        r21 = java.lang.Thread.interrupted();	 Catch:{ all -> 0x0056 }
        if (r21 == 0) goto L_0x025f;
    L_0x022e:
        if (r51 == 0) goto L_0x0239;
    L_0x0230:
        r45 = 100;
        r0 = r51;
        r1 = r45;
        r0.onProgress(r1);	 Catch:{ all -> 0x0056 }
    L_0x0239:
        if (r21 == 0) goto L_0x02be;
    L_0x023b:
        r45 = new java.security.SignatureException;	 Catch:{ all -> 0x0056 }
        r46 = "verification was interrupted";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x0243:
        r45 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0056 }
        r45.<init>();	 Catch:{ all -> 0x0056 }
        r0 = r45;
        r45 = r0.append(r11);	 Catch:{ all -> 0x0056 }
        r46 = "with";
        r45 = r45.append(r46);	 Catch:{ all -> 0x0056 }
        r0 = r45;
        r45 = r0.append(r12);	 Catch:{ all -> 0x0056 }
        r4 = r45.toString();	 Catch:{ all -> 0x0056 }
        goto L_0x01ff;
    L_0x025f:
        r0 = r6.length;	 Catch:{ all -> 0x0056 }
        r38 = r0;
        r0 = r38;
        r0 = (long) r0;	 Catch:{ all -> 0x0056 }
        r46 = r0;
        r46 = r46 + r40;
        r45 = (r46 > r42 ? 1 : (r46 == r42 ? 0 : -1));
        if (r45 <= 0) goto L_0x0274;
    L_0x026d:
        r46 = r42 - r40;
        r0 = r46;
        r0 = (int) r0;	 Catch:{ all -> 0x0056 }
        r38 = r0;
    L_0x0274:
        r45 = 0;
        r0 = r30;
        r1 = r45;
        r2 = r38;
        r31 = r0.read(r6, r1, r2);	 Catch:{ all -> 0x0056 }
        r45 = 0;
        r0 = r32;
        r1 = r45;
        r2 = r31;
        r0.update(r6, r1, r2);	 Catch:{ all -> 0x0056 }
        r0 = r31;
        r0 = (long) r0;	 Catch:{ all -> 0x0056 }
        r46 = r0;
        r40 = r40 + r46;
        if (r51 == 0) goto L_0x0224;
    L_0x0294:
        r28 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0056 }
        r46 = 100;
        r46 = r46 * r40;
        r46 = r46 / r42;
        r0 = r46;
        r0 = (int) r0;	 Catch:{ all -> 0x0056 }
        r25 = r0;
        r0 = r25;
        r1 = r24;
        if (r0 <= r1) goto L_0x0224;
    L_0x02a9:
        r46 = r28 - r26;
        r48 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r45 = (r46 > r48 ? 1 : (r46 == r48 ? 0 : -1));
        if (r45 <= 0) goto L_0x0224;
    L_0x02b1:
        r24 = r25;
        r26 = r28;
        r0 = r51;
        r1 = r24;
        r0.onProgress(r1);	 Catch:{ all -> 0x0056 }
        goto L_0x0224;
    L_0x02be:
        r45 = r33.getEncryptedDigest();	 Catch:{ all -> 0x0056 }
        r0 = r32;
        r1 = r45;
        r45 = r0.verify(r1);	 Catch:{ all -> 0x0056 }
        if (r45 != 0) goto L_0x02d4;
    L_0x02cc:
        r45 = new java.security.SignatureException;	 Catch:{ all -> 0x0056 }
        r46 = "signature digest verification failed";
        r45.<init>(r46);	 Catch:{ all -> 0x0056 }
        throw r45;	 Catch:{ all -> 0x0056 }
    L_0x02d4:
        r30.close();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.rockchip.update.service.RecoverySystem.verifyPackage(java.io.File, android.rockchip.update.service.RecoverySystem$ProgressListener, java.io.File):void");
    }

    public static void installPackage(Context context, File packageFile) throws IOException {
        String filename = packageFile.getCanonicalPath();
        Log.w("RecoverySystem", "!!! REBOOTING TO INSTALL " + filename + " !!!");
        String arg = "--update_package=" + filename + "\n--locale=" + Locale.getDefault().toString();
        writeFlagCommand(filename);
        bootCommand(context, arg);
    }

    public static void installRKimage(Context context, String imagePath) throws IOException {
        Log.w("RecoverySystem", "!!! REBOOTING TO INSTALL rkimage " + imagePath + " !!!");
        String arg = "--update_rkimage=" + imagePath + "\n--locale=" + Locale.getDefault().toString();
        writeFlagCommand(imagePath);
        bootCommand(context, arg);
    }


    public static String readFlagCommand() {
        if (!UPDATE_FLAG_FILE.exists()) {
            return null;
        }
        Log.d("RecoverySystem", "UPDATE_FLAG_FILE is exists");
        char[] buf = new char[128];
        int readCount = 0;
        try {
            readCount = new FileReader(UPDATE_FLAG_FILE).read(buf, 0, buf.length);
            Log.d("RecoverySystem", "readCount = " + readCount + " buf.length = " + buf.length);
        } catch (IOException e) {
            Log.e("RecoverySystem", "can not read /cache/recovery/last_flag!");
        } finally {
            UPDATE_FLAG_FILE.delete();
        }
        StringBuilder sBuilder = new StringBuilder();
        int i = 0;
        while (i < readCount && buf[i] != '\u0000') {
            sBuilder.append(buf[i]);
            i++;
        }
        return sBuilder.toString();
    }

    public static void writeFlagCommand(String path) throws IOException {
        RECOVERY_DIR.mkdirs();
        UPDATE_FLAG_FILE.delete();
        FileWriter writer = new FileWriter(UPDATE_FLAG_FILE);
        try {
            Log.e("RecoverySystem","startToWrite");
            writer.write("updating$path=" + path);
        } finally {
            writer.close();
        }
    }

    private static void bootCommand(Context context, String arg) throws IOException {
        RECOVERY_DIR.mkdirs();
        COMMAND_FILE.delete();
        LOG_FILE.delete();
        FileWriter command = new FileWriter(COMMAND_FILE);
        try {
            command.write(arg);
            command.write("\n");
            ((PowerManager) context.getSystemService("power")).reboot("recovery");
            throw new IOException("Reboot failed (no permissions?)");
        } finally {
            command.flush();
            command.close();
        }
    }
}
