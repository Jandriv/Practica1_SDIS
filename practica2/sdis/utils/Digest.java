package sdis.utils;

import java.util.UUID;

public class Digest {
    public String generateHash() {
        return UUID.randomUUID().toString();
    }
}
