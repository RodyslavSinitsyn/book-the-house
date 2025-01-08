package bth.common.contract;

public interface UserService {
    String registerIfNotExist(String username, String friendlyUsername, String email);
}
