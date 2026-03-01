package tasks;

import common.Person;
import common.PersonService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  //Итоговая сложность алгоритма сортировки коллекции, которая приходит из сервиса - O(M+N)
  //При решении задачи предполагаю, что в personIds и persons не может быть элементов со значением null
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    // Пусть N - количество элементов в persinIds
    Set<Person> persons = personService.findPersons(personIds);//возвращает Set из M элементов; где M<=N

    // Создаем словарь, где ключом является id, а значением объект класса Person - сложность O(M)
    Map<Integer, Person> mapPersons =  persons.stream()
        .collect(Collectors.toMap(
            Person::id,
            Function.identity()
        ));

    // Прохожусь по всем id в листе, который нам изначально передали, для каждого получаю
    // значение из словаря по такому id, пропускаю его дальше в stream, а потом собираю стрим в Лист
    // сложность O(N)
    return personIds.stream()
        .map(mapPersons::get)
        .toList();
  }
}
