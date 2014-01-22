# abstract.mkdir #

Abstrakcyjny szablon przeznaczony dla *Tempel* umożliwiający tworzenie
pojedynczych katalogów w lokalnym systemie plików. Dostarcza klasę silnika
[`org.agiso.tempel.engine.MakeDirEngine`][MakeDirEngine], który tworzy katalog
o nazwie przekazanej poprzez parametr `target`.

## Sposób użycia ##

Do poprawnego wywołania szablon `abstract.mkdir` wymaga zdefiniowania za pomocą
znacznika `target` poprawnej nazwy katalogu, który ma zostać utworzony. Ponieważ
jest szablonem abstrakcyjnym, więc może być używany jedynie jako szablon
referencyjny, a nazwa katalogu do utworzenia musi być przekazana z szablonu
nadrzędnego:

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

Jeżeli klasa silnika [`MakeDirEngine`][MakeDirEngine] jest dostępna bezpośrednio
na ścieżce uruchomieniowej, to może on zostać użyty jawnie w definicji własnego
szablonu:

	<template engine="org.agiso.tempel.engine.MakeDirEngine">
		...

		<params>
			<param key="name" name="Directory name" />
		</params>

		<resource>
			<target>${name}</target>
		</resource>
	</template>

[MakeDirEngine]: src/main/java/org/agiso/tempel/engine/MakeDirEngine.java
