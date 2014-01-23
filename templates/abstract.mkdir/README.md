# abstract.mkdir #

Abstrakcyjny szablon umożliwiający utworzenie **pojedynczego** folderu w
katalogu roboczym lokalnego systemu plików. Dostarcza klasę silnika
[`org.agiso.tempel.engine.MakeDirEngine`][MakeDirEngine], który tworzy folder
o nazwie przekazanej poprzez znacznik `<target>` sekcji `<resource>`.

Przeznaczony do użycia w definicjach szablonów jako szablon referencyjny.

## Sposób użycia ##

Do poprawnego wywołania szablon `abstract.mkdir` wymaga zdefiniowania za pomocą
znacznika `<target>` poprawnej nazwy katalogu, który ma zostać utworzony.
Ponieważ jest szablonem abstrakcyjnym, więc może być używany jedynie jako
szablon referencyjny, a nazwa katalogu do utworzenia musi być przekazana z
szablonu nadrzędnego:

```xml
<template>
	...

	<params>
		<param key="name" name="Directory name" />
	</params>

	<references>
		<reference>
			<groupId>org.agiso.tempel.templates</groupId>
			<templateId>abstract.mkdir</templateId>
			<version>1.0.0</version>

			<resource>
				<target>${top.name}</target>
			</resource>
		</reference>
	</references>
</template>
```

Jeżeli klasa silnika [`MakeDirEngine`][MakeDirEngine] jest dostępna bezpośrednio
na ścieżce uruchomieniowej, to może on zostać jawnie użyta w definicji szablonu:

```xml
<template engine="org.agiso.tempel.engine.MakeDirEngine">
	...

	<params>
		<param key="name" name="Directory name" />
	</params>

	<resource>
		<target>${name}</target>
	</resource>
</template>
```

### Zasób wejściowy ###

Szablon `abstract.mkdir` nie przetwarza żadnego zasobu wejściowego.

### Zasób wyjściowy ###

`abstract.mkdir` tworzy w katalogu roboczym pojedynczy folder. Katalog roboczy
musi istnieć i być dostępny do zapisu.


[MakeDirEngine]: src/main/java/org/agiso/tempel/engine/MakeDirEngine.java
