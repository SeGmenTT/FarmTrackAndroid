# FarmTrack (Android, Kotlin/Compose)

Yerel Room veritabanlı hayvan takip uygulaması + WhatsApp bildirim sunucusuyla senkron.
- Sekmeler: Hayvanlar, Hızlı Kayıt, Uyarılar, Ayarlar
- Room: Animal, Milk, Weight, Health, Breed, Task
- Uyarılar: 30 gün penceresi + doğum + kuruya alma (60 gün kala)
- Ayarlar: Backend URL, anında senkron
- WorkManager: 12 saatte bir arkaplan senkron (internet varsa)
- Retrofit: /api/sync, /api/test, /api/notify-now

## Kurulum
- Android Studio ile açın, Gradle senkron olsun, derleyip cihaza yükleyin.
- Ayarlar sekmesinden WhatsApp backend URL’sini girin (örn `http://192.168.1.10:8787`).

Not: WhatsApp mesajlarını uygulama **doğrudan** göndermez; token güvenliği için sunucu üzerinden gönderilir.


## GitHub Actions ile APK alma (Android Studio gerekmez)
1) Bu projeyi yeni bir GitHub reposuna yükle (adı ör. `FarmTrackAndroid`).
2) Repo `main` veya `master` dalına **push** yap.
3) GitHub → **Actions** sekmesine gir; `Android CI (Build APK)` çalışacak.
4) İş akışı bitince **Artifacts** kısmından `FarmTrack-debug-apk` içindeki `.apk` dosyasını indir.
5) Telefonda `Ayarlar > Güvenlik > Bilinmeyen kaynaklara izin ver` ayarla ve APK'yı kur.
