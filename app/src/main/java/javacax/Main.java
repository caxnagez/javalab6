package javacax;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Main {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();

        try (Reader reader = new FileReader("app\\src\\main\\resources\\books.json")) {
            Type listType = new TypeToken<List<Visitor>>() {}.getType();
            List<Visitor> visitors = gson.fromJson(reader, listType);
// Задание 1
            System.out.println("Zadanie 1:");
            for(int i = 0; i<visitors.size(); i++){
                System.out.println(visitors.get(i).getName() + " " + visitors.get(i).getSurname());
            }
            long visitorCount = visitors.size();
            System.out.println("Всего: " + visitorCount);
            System.out.println();
// Задание 2
            System.out.println("Zadanue 2: ");
            List<Book> uniqueBooks = visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .distinct()
                .toList();
                 System.out.println("Уникальные книжки: " + uniqueBooks.size());
                uniqueBooks.forEach(System.out::println);
// Задание 3

            System.out.println("Zadanie 3: ");
          uniqueBooks.stream()
            .sorted(Comparator.comparing(book -> book.getPublishingYear()))
            .forEach(System.out::println);

// Задание 4
            System.out.println("Zadanie 4: ");
            boolean hasJaneAustenBook = uniqueBooks.stream()
                    .anyMatch(book -> book.getAuthor().equals("Jane Austin"));
                System.out.println("Jane`s Autsin books: " + hasJaneAustenBook);

// Задание 5
            System.out.println("Zadanie 5: ");
            int maxFavoriteBooks = visitors.stream()
                    .map(visitor -> visitor.getFavoriteBooks().size())
                    .max(Comparator.naturalOrder())
                    .orElse(0);
            System.out.println("Maximum favorite books: " + maxFavoriteBooks);
            System.out.println();

// Задание 6
            System.out.println("Zadanie 6: ");
            double averageFavoriteBooks = visitors.stream()
                    .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                    .average()
                    .orElse(0);

            Map<String, List<Sms>> smsMessagesByCategory = visitors.stream()
                    .filter(Visitor::isSubscribed)
                    .collect(Collectors.groupingBy(
                            visitor -> {
                                int favoriteBookCount = visitor.getFavoriteBooks().size();
                                if (favoriteBookCount > averageFavoriteBooks) {
                                    return "bookworm";
                                } else if (favoriteBookCount < averageFavoriteBooks) {
                                    return "read more";
                                } else {
                                    return "fine";
                                }
                            },
                            Collectors.mapping(visitor -> new Sms(
                                            visitor.getPhone(),
                                            getSmsMessage(visitor, averageFavoriteBooks)),
                                    Collectors.toList())));

            smsMessagesByCategory.forEach((category, smsMessages) -> {
                System.out.println("Category: " + category);
                smsMessages.forEach(smsMessage -> System.out.println("Phone: " + smsMessage.getPhoneNumber() +
                        ", Message: " + smsMessage.getMessage()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getSmsMessage(Visitor visitor, double averageFavoriteBooks) {
        int favoriteBookCount = visitor.getFavoriteBooks().size();
        return switch (favoriteBookCount > averageFavoriteBooks ? 1 :
                favoriteBookCount < averageFavoriteBooks ? 2 : 3) {
            case 1 -> "you are a bookworm";
            case 2 -> "read more";
            case 3 -> "fine";
            default -> "";
        };
    }
}