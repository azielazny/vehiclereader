Lista rzeczy do zrobienia:

TODO List:
- Zreformatować API, aby wyświetlało pole statusu. Teraz zwraca tylko dane do projektu.
- klucz do API ukryć w pliku
- Poprawić odczyt z kamery. Nie dekoduje słabej jakości zdjęć

- UWAGA! W ostatecznej wersji należy zmodyfikować ścieżki dostępu i zapisu plików graficznych. Testowane było z grafiką w głównym katalogu.


Funkcjonalności:
- Branch Test używa bibliotek OpenCV i manipuluje zdjęciem po odczycie z kamery. Nie ma specjalnej poprawy w odczycie.
- WebcamCapture zczytuje zdjęcia offline z kamery i w lini webcam = Webcam.getWebcams().get(0); można odpalić kolejne kamery
- App to wersja offline apki w terminalu
- Client to przykładowa implementacja dostępu do api. Główny servlet to WebInterfaceServlet
- Servlet ma zaimplementowane: skanowanie dowodu rej. z kamery, skanowanie z załadowanego zdjęcia, odczyt z wklejonego zaszyfrowanego kodu ze skanera.

- Wersja webowa odczytuje kody i stara się wyświetlić sformatowane dane. Jest jednak możliwość odczytu różnego typu kodów QR, EAN, Aztec itp. Nie jest to włączone w API, ale działa offline.
- Podobnie jest z odczytem z pliku. Jest zaimplementowene, ale nie włączone na serwlecie dla klienta.
