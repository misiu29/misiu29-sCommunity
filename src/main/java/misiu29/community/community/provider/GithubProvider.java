package misiu29.community.community.provider;

import com.alibaba.fastjson.JSON;
import misiu29.community.community.dto.AccessTokenDto;
import misiu29.community.community.dto.GithubUserDto;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDto accessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            /*
            String[] split = string.split("&");
            String tokenStr = split[0];
            String token = tokenStr.split("=")[1];
            */
            String token=string.split("&")[0].split("=")[1];
            System.out.println("1-"+token);
            return token;
        } catch (IOException e) {
        }
        return null;
    }

    public GithubUserDto getUser(String accessToken){
        System.out.println("2-"+accessToken);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("https://api.github.com/user?access_token="+accessToken)
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            System.out.println("4-"+string);
            GithubUserDto githubUserDto = JSON.parseObject(string, GithubUserDto.class);
            return githubUserDto;
        } catch (IOException e) {
        }
        return null;
    }
}
