package edu.practicum.general;

import edu.practicum.models.User;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Random;
import java.util.stream.Stream;

import static edu.practicum.data.UtilsForDataPrepare.emailRandom;
import static edu.practicum.data.UtilsForDataPrepare.stringRandomGenerate;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestDataForUser {
    public static Stream<Arguments> getCorrectUserData() {
        Random rnd = new Random();
        return Stream.of(
                arguments(new User.Builder()
                        .email(emailRandom(rnd.nextInt(254) + 1))
                        .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .name(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build())
        );
    }

    public static Stream<Arguments> getEqualUserData() {
        Random rnd = new Random();
        User user1 = new User.Builder()
                .email(emailRandom(rnd.nextInt(254) + 1))
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .name(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        User user2 = new User.Builder()
                .email(user1.getEmail())
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .name(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        return Stream.of(
                arguments(user1, user1),
                arguments(user1, user2)
        );
    }

    public static Stream<Arguments> getUserDataWithoutRequiredField() {
        Random rnd = new Random();
        return Stream.of(
                arguments(new User.Builder()
                        .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .name(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new User.Builder()
                        .email(emailRandom(rnd.nextInt(254) + 1))
                        .name(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new User.Builder()
                        .email(emailRandom(rnd.nextInt(254) + 1))
                        .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                        .build()),
                arguments(new User.Builder()
                        .build())
        );
    }
}
