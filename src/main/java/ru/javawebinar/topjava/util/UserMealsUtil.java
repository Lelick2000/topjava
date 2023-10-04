package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        //System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        HashMap<LocalDate, Integer> daysCalories = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        List<UserMeal> filteredMeals = new ArrayList<>();
        List<LocalDate> daysExceed = new ArrayList<>();

        for (UserMeal meal : meals) {
            if (meal.getDateTime().toLocalTime().isAfter(startTime) && meal.getDateTime().toLocalTime().isBefore(endTime)) {
                daysCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
                filteredMeals.add(meal);

                if (daysCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay) {
                    daysExceed.add(meal.getDateTime().toLocalDate());
                }
            }
        }

        for (UserMeal meal : filteredMeals) {
            if (daysExceed.contains(meal.getDateTime().toLocalDate())) {
                userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
            }
        }

        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return meals.stream()
                .filter(el -> el.getDateTime().toLocalTime().isAfter(startTime) && el.getDateTime().toLocalTime().isBefore(endTime))
                .map(el -> new UserMealWithExcess(el.getDateTime(), el.getDescription(), el.getCalories(), true))
                .collect(Collectors.toList());
    }
}
