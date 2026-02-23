package com.mes.eld_log.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ELdUtils {

    private static final Logger log = LoggerFactory.getLogger(ELdUtils.class);

    /**
     * FMCSA Character Contribution (Table-3)
     * A–Z, a–z, 0–9 => ASCII - 48
     * Everything else => 0
     */
    public static int charContribution(char ch) {
        int code = (int) ch;

        boolean isUpper = code >= 'A' && code <= 'Z';
        boolean isLower = code >= 'a' && code <= 'z';
        boolean isDigit = code >= '0' && code <= '9';

        if (isUpper || isLower || isDigit) {
            return (code - 48) & 0xFF;
        }
        return 0;
    }

    /** Rotate left by 3 bits (circular) */
    private static int rol3(int value) {
        return ((value << 3) | (value >> 5)) & 0xFF;
    }

    /** Compute Line Data Check Value (LDCV). XOR with 0x96 (FMCSA) */
    public static String computeLineDataCheck(String lineWithoutCheckField) {
        if (lineWithoutCheckField == null) return "00";

        int sum = 0;
        for (char ch : lineWithoutCheckField.toCharArray()) {
            sum += charContribution(ch);
        }

        int low8 = sum & 0xFF;
        int rotated = rol3(low8);
        int finalVal = rotated ^ 0x96;

        return String.format("%02X", finalVal);
    }

    /** Compute Event Data Check Value (same algo as LDCV) */
    public static String computeEventDataCheck(String... fields) {
        int sum = 0;

        for (String f : fields) {
            if (f == null) continue;
            for (char ch : f.toCharArray()) {
                sum += charContribution(ch);
            }
        }

        int low8 = sum & 0xFF;
        int rotated = rol3(low8);
        int finalVal = rotated ^ 0x96;

        return String.format("%02X", finalVal);
    }

    /**
     * Compute File Data Check Value (FDCV)
     * 16-bit value, rotate each byte left by 3 without swapping bytes.
     */
    public static String computeFileDataCheck(List<String> lineChecks) {
        int total = 0;
        for (String hex : lineChecks) {
            if (hex == null) continue;
            String s = hex.trim();
            if (s.isEmpty()) continue;
            total += Integer.parseInt(s, 16) & 0xFF;
        }

        int value = total & 0xFFFF;

        int low = value & 0xFF;
        int high = (value >> 8) & 0xFF;

        int rotatedLow = rol3(low);
        int rotatedHigh = rol3(high);

        // Keep byte positions
        int rotated16 = (rotatedHigh << 8) | rotatedLow;

        int finalVal = rotated16 ^ 0x969C;
        return String.format("%04X", finalVal);
    }

    /** Load RSA Private Key (classpath PEM) */
    public PrivateKey loadPrivateKey(String filename) throws Exception {
        // NOTE: you are ignoring filename in your current code, keeping same behavior
        ClassPathResource resource = new ClassPathResource("pem_file/private_key.pem");

        if (!resource.exists())
            throw new FileNotFoundException("Private key not found");

        String keyPem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyPem);

        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    /** Generate ELD Authentication Value (SHA256withRSA -> HEX) */
    public String generateEldAuthenticationValue(PrivateKey privateKey, String textToSign) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(textToSign.getBytes(StandardCharsets.US_ASCII));

        byte[] sig = signature.sign();

        StringBuilder hex = new StringBuilder();
        for (byte b : sig) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString().toUpperCase();
    }

    // ------------------------------------------------------------------
    // ✅ FMCSA Event Sequence ID Generator (NO DB, persistent file)
    // Rules:
    // - continuous across power cycles (persist last in file)
    // - increment +1 per new event
    // - range 0..FFFF, wrap after FFFF -> 0
    // - output HEX uppercase, 1-4 chars (no padding)
    // ------------------------------------------------------------------
  

    public static final class TruckSeqCounter {

        private final Path file;
        private int last; // 0..65535

        public TruckSeqCounter(Path file) {
            this.file = file;
            this.last = load();
        }

        /** Next sequence as HEX (uppercase). 1..FFFF then 0.. */
        public synchronized String nextHex() {
            last = (last + 1) & 0xFFFF; // FFFF -> 0
            save(last);
            // 1-4 chars (spec allows 1-4)
            return Integer.toHexString(last).toUpperCase();
            // If your validator prefers 4 chars, use:
            // return String.format("%04X", last);
        }

        private int load() {
            try {
                if (!Files.exists(file)) return 0; // factory initial
                String s = Files.readString(file).trim();
                if (s.isEmpty()) return 0;
                return Integer.parseInt(s) & 0xFFFF; // stored decimal
            } catch (Exception e) {
                return 0;
            }
        }

        private void save(int v) {
            try {
                Path parent = file.getParent();
                if (parent != null) Files.createDirectories(parent);

                Files.writeString(
                        file,
                        String.valueOf(v & 0xFFFF),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
            } catch (IOException ignored) {}
        }
    }

}