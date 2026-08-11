# â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
#  check-loopback.ps1  â€”  JVM loopback probe
#
#  Verifies that java.nio.channels.Selector.open() works. Appium/Selenium need
#  it to open a device session. If this FAILS, no Java Appium test can run until
#  the machine's Winsock/loopback is fixed (netsh winsock reset, as admin + reboot).
#
#  Run from anywhere:   .\check-loopback.ps1
# â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•

$dir = Join-Path $env:TEMP "loopbackprobe"
New-Item -ItemType Directory -Force -Path $dir | Out-Null
$src = Join-Path $dir "LoopbackProbe.java"

@'
import java.nio.channels.Selector;
public class LoopbackProbe {
  public static void main(String[] a) throws Exception {
    logpoint("Java: " + System.getProperty("java.version"));
    try {
      Selector s = Selector.open(); s.close();
      logpoint("RESULT: OK  - Selector.open() works. Appium can create sessions.");
    } catch (Exception e) {
      logpoint("RESULT: FAIL - " + e);
      logpoint("Loopback is blocked. Fix: run 'netsh winsock reset' + 'netsh int ip reset'");
      logpoint("in an ADMIN terminal, then REBOOT. If it still fails, endpoint security");
      logpoint("is blocking loopback for java.exe -> ask IT to allowlist it.");
    }
  }
}
'@ | Set-Content -Encoding ascii $src

& javac -d $dir $src
if ($LASTEXITCODE -ne 0) { Write-Host "javac failed - is the JDK on PATH?" -ForegroundColor Red; exit 1 }
& java -cp $dir LoopbackProbe

