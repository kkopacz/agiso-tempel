agiso-tempel [![Build Status](https://travis-ci.org/kkopacz/agiso-tempel.png?branch=master)](https://travis-ci.org/kkopacz/agiso-tempel)
============

Agiso Tempel jest narzędziem umożliwiającym generację różnego typu zasobów w
oparciu o *szablony*. Szablony wykorzystują *zasoby wejściowe* przechowywane
w repozytoriach (także w repozytoriach Apache Maven) oraz *wartości parametrów*
dostarczane w trakcie wykonywania np. z konsoli systemowej, w której
uruchomiono Tempel. Opisywane są za pomocą języka XML w tzw.
*definicjach szablonów*.

Logika wykorzystywana do przetwarzania zasobów wejściowych na wyjściowe zawarta
jest w *silinkach generacji*. Tempel dostarcza własnych implementacji silników,
które umożliwiają m.in. generację zasobów wyjściowych z wykorzystaniem Apache
Velocity. Ponadto umożliwia tworzenie własnych szablonów, wykorzystujacych
dedykowane lub dostarczone silniki, oraz rozpowszechnianie ich poprzez
repozytoria Apache Maven.

Zastosowanie
------------
Temple może być z powodzeniem wykorzystywany do takich celów jak:

* tworzenie złożonych struktur dyskowych składających się z wielu katalogów
  i plików, np. projekty programistyczne, strony WWW, etc.,

* rozbudowa istniejących stuktur dyskowych poprzez dodawanie nowych katalogów,
  tworzenie nowych plików i rozbudowę teści istniejących plików,

* transformacje danych,

* ...

Jak zacząć
----------
Agiso Tempel w formie narzędzia linii komend dostępny jest jako archiwum 
[tar.gz][tempel.tar.gz] lub [zip][tempel.zip] w sekcji [releases][releases].
Instalacja polega na pobraniu odpowiedniego archiwum i jego rozpakowaniu
w dogodnej lokalizacji systemu plików. Po wykonaniu tej czynności można
używać z polecenia `tpl`, wykorzystywać dostępne szablony i tworzyć własne.

[tempel.tar.gz]: releases/download/v0.0.1.RC1/tempel-0.0.1.RC1.tar.gz
[tempel.zip]: releases/download/v0.0.1.RC1/tempel-0.0.1.RC1.zip
[releases]: releases
