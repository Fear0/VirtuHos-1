# Environment zum Testen erstellen

Um den Parser und die Analyse grob testen zu können ist es erforderlich einige Testmeetings und User auf dem BBB Server zu erstellen. Die Klasse DemoEnvironment ist alleine ausführbar und kann vor dem eigentlichen Testen gestartet werden. 
Am Anfang der Klasse befinden sich einige private Attribute, welche nach Bedarf eingestellt werden können. Die restliche Klasse muss nicht verändert werden.
Nach Ausführung der DemoEnvironment Klasse werden Meetings erstellt und User joinen automatisiert über den lokalen Browser. Es ist wichtig, dass der Browser danach offen bleibt, da die User sonst wieder den Server verlassen. 

Mit folgendem Link kann man sich alle Meetings auf dem Server anzeigen lassen: https://bbb2.se.uni-hannover.de/bigbluebutton/api/getMeetings?checksum=6be2130d0d2a1b06fcd62d6afdea210c8cc8e5cf

**Achtung: Da sich alle Teams eine BBB Instanz teilen können Meetings anderer Teams ebenfalls auftauchen und Testergebnisse beeinflussen.**
