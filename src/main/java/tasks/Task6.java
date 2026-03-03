package tasks;

import common.Area;
import common.Person;

import java.util.*;
import java.util.stream.Collectors;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 {

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    // Создаю словарь (id региона - Название региона)
    Map<Integer, String> areasNameById = areas
        .stream()
        .collect(Collectors.toMap(
            Area::getId,
            Area::getName));


    return persons
        .stream()
        .flatMap(person -> {
          Set<Integer> areaIds = personAreaIds.get(person.id()); // Получаю регионы персоны
          String personName = person.firstName(); // Получаю имя персоны

          // Для каждого региона персоны создаю строчку типа "Имя - Регион", и в итоге возвращаю Stream из таких строк
          return areaIds.stream()
              .map(areaId -> personName + " - " + areasNameById.get(areaId));
        })
        .collect(Collectors.toSet());
  }
}
