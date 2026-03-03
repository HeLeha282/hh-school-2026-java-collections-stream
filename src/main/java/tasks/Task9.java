package tasks;

import common.Person;
import jdk.jshell.DeclarationSnippet;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    // Проверку на пустоту листа можно убрать,
    // потому что StreamApi просто вернет пустую коллекцию в крайнем случае, а не упадет

    // также skip лучше, чем remove тем, что не изменяет объект переданный в качестве параметра.
    // И тем, что если бы передали ArrayList, то remove(0) имело бы асимптотику O(n), а skip просто пропустит 1 эл-т.
    return persons.stream()
        .skip(1)
        .map(Person::firstName)
        .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // Стримы лишние, можно просто воспользоваться конструктором HashSet, он обеспечит уникальность имен.
    // Удаление фальшивой персоны уже реализовано в getNames, поэтому тут делать не нужно.
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    //перепишу на стримы и заменю дублирование фамилии на middleName
    return Stream.of(person.firstName(),person.secondName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    //Перепишу на StreamApi, потому что тогда не придется создавать промежуточную переменную.
    // Также в изначальном коде идет проверка на то что ключа нет в мапе
    // => могут быть дубликаты и в таком случае нужно сохранять старый ключ,
    // поэтому нужно реализовать мердж версию Collectors.toMap
    return persons.stream()
        .collect(Collectors.toMap(
            Person::id,
            this::convertPersonToString,
            (oldKey, newKey) -> oldKey
        ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    //Перепишу на стримы, потому что получается красивее.
    //Еще один плюс - anyMatch имеет короткое замыкание и сразу возвращает true, как увидит первую совпадающую персону
    //Также для проверки наличия персоны в коллекции преобразую одну из них в Set,
    //получается что тратим больше памяти, но при этом сложность меняется с O(n*m) на O(n+m)
    Set<Person> personsSet2 = new HashSet<>(persons2);
    return persons1.stream().anyMatch(personsSet2::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    //Переписал на стримы без использования вспомогательной переменной и сразу поместил в return
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке

  // Ответ: потому что HashSet создается с capacity больше чем количество помещаемых в него при создании элементов.
  // HashCode для Integer равен самому числу, следовательно, не будет HashCode-ов больших чем capacity,
  // а так как HashSet добавляет в элемент в корзину по индексу HashCode & (capacity - 1),
  // то этот индекс всегда будет равен значению числа и равен HashCode,
  // Следовательно все значения просто поместятся в бакеты соответсвующие им по их значению.
  // А при toString происходит итерация по бакетам по порядку и получается строка со значения из бакетов по порядку.
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
