package com.capillary.ops.service;

import com.capillary.ops.bo.SSHKeyPair;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
public class SSHKeyPairGenerator {

  public SSHKeyPair generate() {
    try {
      JSch jsch = new JSch();
      KeyPair kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA, 2048);
      ByteArrayOutputStream publicKeyOStream = new ByteArrayOutputStream();
      ByteArrayOutputStream privateKeyOStream = new ByteArrayOutputStream();
      kpair.writePrivateKey(privateKeyOStream);
      kpair.writePublicKey(publicKeyOStream, "deis");
      return new SSHKeyPair(
          privateKeyOStream.toString("UTF-8"), publicKeyOStream.toString("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
