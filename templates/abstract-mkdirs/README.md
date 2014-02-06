# abstract.mkdirs #

Szablon abstrakcyjny przeznaczony do użycia w definicjach szablonów w sekcji
`<references>`. Działa analogicznie do szablonu [`abstract.mkdir`][mkdir]
z tą różnicą, że umożliwia tworzenie całej ścieżki folderów (tj. folderu wraz
z jego folderami nadrzędnymi jeśli te nie istnieją). Dostarcza klasę silnika
[`org.agiso.tempel.engine.MakeDirsEngine`][MakeDirsEngine], który tworzy
ścieżkę zdefiniowaną poprzez znacznik `<target>` sekcji `<resource>`.



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
			<templateId>abstract-mkdirs</templateId>
			<version>1.0.0</version>

			<resource>
				<target>${top.path}</target>
			</resource>
		</reference>
	</references>
</template>
```

Jeżeli klasa silnika [`MakeDirsEngine`][MakeDirsEngine] znajduje się na ścieżce
uruchomieniowej, to może zostać jawnie użyta w definicji szablonu:

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

### Zasób wejściowy ###

Szablon `abstract.mkdirs` nie przetwarza żadnego zasobu wejściowego.

### Zasób wyjściowy ###

`abstract.mkdirs` tworzy w katalogu roboczym ścieżkę folderów. W razie potrzeby
trworzy foldery nadrzędne dla folderu ostatniego poziomu.


[mkdir]: ../abstract-mkdir/README.md#abstractmkdir
[MakeDirsEngine]: src/main/java/org/agiso/tempel/engine/MakeDirsEngine.java
