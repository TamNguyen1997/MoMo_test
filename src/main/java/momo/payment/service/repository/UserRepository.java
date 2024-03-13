package momo.payment.service.repository;

import momo.payment.service.model.User;
import momo.payment.service.utils.ParseUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class UserRepository {
  public static final String FILE_PATH = "src/main/resources/user.txt";
  public static final User user = new User();

  static {
    try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
      user.setBalance(ParseUtil.parseInt(lines.findFirst().orElseThrow()));
    } catch (IOException e) {
      System.out.println("ERROR");
    }
  }
}
