# abstract.mkdirs #

Abstrakcyjny szablon przeznaczony dla *Tempel*. Działa analogicznie do szablonu
[`abstract.mkdir`][mkdir] z tą różnicą, że umożliwia tworzenie ścieżek katalogów
(tj. katalogu wraz z jego katalogami nadrzędnymi jeśli te nie istnieją).
Dostarcza klasę silnika [`org.agiso.tempel.engine.MakeDirsEngine`]
[MakeDirsEngine], który tworzy ścieżkę definiowaną poprzez parametr `target`.

## Sposób użycia ##

Szablon `abstract.mkdirs` jest szablonem abstrakcyjnym, więc może być używany
jedynie jako szablon referencyjny:

```xml
<template>
	...

	<params>
		<param key="path" name="Directory path" />
	</params>

	<references>
		<reference>
			<groupId>org.agiso.tempel.templates</groupId>
			<templateId>abstract.mkdirs</templateId>
			<version>1.0.0</version>

			<resource>
				<target>${top.path}</target>
			</resource>
		</reference>
	</references>
</template>
```

Jeżeli klasa silnika [`MakeDirsEngine`][MakeDirsEngine] znajduje sięna ścieżce
uruchomieniowej, to może on zostać użyty jawnie w definicji własnego szablonu:

```xml
<template engine="org.agiso.tempel.engine.MakeDirsEngine">
	...

	<params>
		<param key="path" name="Directory path" />
	</params>

	<resource>
		<target>${path}</target>
	</resource>
</template>
```

[mkdir]: ../abstract.mkdir/
[MakeDirsEngine]: src/main/java/org/agiso/tempel/engine/MakeDirsEngine.java
