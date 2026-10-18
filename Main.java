import java.util.Scanner;

public class Main {

    static final double TARIF_PAJAK = 0.10;     
    static final int BIAYA_LAYANAN = 20000;     
    static final int BATAS_DISKON = 100000;     
    static final double PERSEN_DISKON = 0.10;
    static final int BATAS_PROMO = 50000;     

    static Scanner input = new Scanner(System.in);
    static Menu[] daftarMenu = new Menu[8];    

    static String[] namaPesanan = new String[4];
    static String[] kategoriPesanan = new String[4];
    static int[] hargaPesanan = new int[4];
    static int[] jumlahPesanan = new int[4];
    static int[] totalPesanan = new int[4];

    public static void main(String[] args) {
        isiMenu();
        tampilkanMenu();

        int jumlahMenu = bacaJumlahMenu();
        if (jumlahMenu == 0) {
            System.out.println("Input tidak valid. Masukkan angka 1 sampai 4. Program berhenti.");
        } else {
            terimaPesanan(jumlahMenu);
            prosesDanCetakStruk();
        }
    }

    static void isiMenu() {
        daftarMenu[0] = new Menu("Nasi Padang", 25000, "Makanan");
        daftarMenu[1] = new Menu("Ayam Bakar", 22000, "Makanan");
        daftarMenu[2] = new Menu("Mie Goreng", 18000, "Makanan");
        daftarMenu[3] = new Menu("Sate Ayam", 20000, "Makanan");
        daftarMenu[4] = new Menu("Es Teh Manis", 5000, "Minuman");
        daftarMenu[5] = new Menu("Es Jeruk", 8000, "Minuman");
        daftarMenu[6] = new Menu("Kopi Susu", 15000, "Minuman");
        daftarMenu[7] = new Menu("Jus Alpukat", 14000, "Minuman");
    }

    static void tampilkanMenu() {
        System.out.println(garis());
        System.out.println("           DAFTAR MENU RESTORAN");
        System.out.println(garis());
        System.out.println("[ MAKANAN ]");
        tampilBarisMenu(0);
        tampilBarisMenu(1);
        tampilBarisMenu(2);
        tampilBarisMenu(3);
        System.out.println();
        System.out.println("[ MINUMAN ]");
        tampilBarisMenu(4);
        tampilBarisMenu(5);
        tampilBarisMenu(6);
        tampilBarisMenu(7);
        System.out.println(garis());
    }

    static void tampilBarisMenu(int i) {
        System.out.printf("%d. %-16s %12s%n", (i + 1), daftarMenu[i].getNama(), rp(daftarMenu[i].getHarga()));
    }

    static int bacaJumlahMenu() {
        System.out.print("Berapa jenis menu yang ingin dipesan (maks. 4)? ");
        String teks = input.nextLine().trim();
        if (teks.matches("[1-4]")) {
            return Integer.parseInt(teks);
        } else {
            return 0;
        }
    }

    static void terimaPesanan(int jumlahMenu) {
        System.out.println("Format input: Nama Menu = jumlah   (contoh: Nasi Padang = 2)");
        bacaPesanan(0);
        if (jumlahMenu >= 2) {
            bacaPesanan(1);
            if (jumlahMenu >= 3) {
                bacaPesanan(2);
                if (jumlahMenu == 4) {
                    bacaPesanan(3);
                }
            }
        }
    }

    static void bacaPesanan(int slot) {
        System.out.print("Pesanan ke-" + (slot + 1) + ": ");
        String baris = input.nextLine();

        String[] bagian = baris.split("=");
        if (bagian.length != 2) {
            System.out.println("  -> Format salah, pesanan ini dilewati.");
            return;
        }

        String nama = bagian[0].trim();
        String teksJumlah = bagian[1].trim();

        if (!teksJumlah.matches("\\d{1,3}")) {
            System.out.println("  -> Jumlah tidak valid, pesanan ini dilewati.");
            return;
        }
        int jumlah = Integer.parseInt(teksJumlah);
        if (jumlah <= 0) {
            System.out.println("  -> Jumlah harus lebih dari 0, pesanan ini dilewati.");
            return;
        }

        int indeks = cariIndeksMenu(nama);
        if (indeks == -1) {
            System.out.println("  -> Menu \"" + nama + "\" tidak ditemukan, pesanan ini dilewati.");
            return;
        }

        Menu m = daftarMenu[indeks];
        namaPesanan[slot] = m.getNama();
        kategoriPesanan[slot] = m.getKategori();
        hargaPesanan[slot] = m.getHarga();
        jumlahPesanan[slot] = jumlah;
        totalPesanan[slot] = m.getHarga() * jumlah;
    }

    static int cariIndeksMenu(String nama) {
        switch (nama.toLowerCase()) {
            case "nasi padang":
                return 0;
            case "ayam bakar":
                return 1;
            case "mie goreng":
                return 2;
            case "sate ayam":
                return 3;
            case "es teh manis":
                return 4;
            case "es jeruk":
                return 5;
            case "kopi susu":
                return 6;
            case "jus alpukat":
                return 7;
            default:
                return -1;
        }
    }

    static void prosesDanCetakStruk() {
        long subtotal = (long) totalPesanan[0] + totalPesanan[1] + totalPesanan[2] + totalPesanan[3];

        if (subtotal == 0) {
            System.out.println("Tidak ada pesanan yang valid. Struk tidak dicetak.");
            return;
        }

        boolean dapatDiskon = false;
        boolean dapatPromo = false;
        if (subtotal > BATAS_DISKON) {
            dapatDiskon = true;    
            dapatPromo = true;
        } else if (subtotal > BATAS_PROMO) {
            dapatPromo = true;     
        }

        long diskon = 0;
        if (dapatDiskon) {
            diskon = Math.round(subtotal * PERSEN_DISKON);
        }
        long hargaSetelahDiskon = subtotal - diskon;
        long pajak = Math.round(subtotal * TARIF_PAJAK);
        long totalBayar = hargaSetelahDiskon + pajak + BIAYA_LAYANAN;

        System.out.println();
        System.out.println(garis());
        System.out.println("              STRUK PESANAN");
        System.out.println(garis());
        System.out.printf("%-18s %2s   %10s   %12s%n", "Item", "Qty", "Harga", "Total");
        System.out.println(garisTipis());
        cetakBarisItem(0);
        cetakBarisItem(1);
        cetakBarisItem(2);
        cetakBarisItem(3);
        System.out.println(garisTipis());

        cetakBaris("Total biaya pesanan", rp(subtotal));

        if (dapatDiskon) {
            cetakBaris("Diskon 10% (total > Rp 100.000)", "-" + rp(diskon));
            cetakBaris("Harga setelah diskon", rp(hargaSetelahDiskon));
        }

        cetakBaris("Pajak 10% (dari total pesanan)", rp(pajak));
        cetakBaris("Biaya pelayanan", rp(BIAYA_LAYANAN));
        System.out.println(garisTipis());
        cetakBaris("TOTAL BAYAR", rp(totalBayar));
        System.out.println(garis());

        if (dapatPromo) {
            int slot = cariSlotMinuman();
            if (slot == -1) {
                System.out.println("Penawaran Beli 1 Gratis 1 Minuman: tidak berlaku");
                System.out.println("(tidak ada minuman pada pesanan).");
            } else {
                System.out.println("PENAWARAN BELI 1 GRATIS 1 (Minuman)");
                System.out.println("Gratis: " + namaPesanan[slot] + " x" + jumlahPesanan[slot]);
                System.out.println("Nilai bonus: " + rp((long) hargaPesanan[slot] * jumlahPesanan[slot]));
            }
        } else {
            System.out.println("Tidak ada diskon / penawaran khusus.");
        }
        System.out.println(garis());
        System.out.println("      Terima kasih atas kunjungan Anda!");
        System.out.println(garis());
    }

    static void cetakBarisItem(int slot) {
        if (jumlahPesanan[slot] > 0) {
            System.out.printf("%-18s %2d x %10s = %12s%n",
                    namaPesanan[slot], jumlahPesanan[slot],
                    rp(hargaPesanan[slot]), rp(totalPesanan[slot]));
        }
    }

    static int cariSlotMinuman() {
        if ("Minuman".equals(kategoriPesanan[0])) {
            return 0;
        } else if ("Minuman".equals(kategoriPesanan[1])) {
            return 1;
        } else if ("Minuman".equals(kategoriPesanan[2])) {
            return 2;
        } else if ("Minuman".equals(kategoriPesanan[3])) {
            return 3;
        } else {
            return -1;
        }
    }

    static void cetakBaris(String label, String nilai) {
        System.out.printf("%-34s %14s%n", label, nilai);
    }

    static String rp(long angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }

    static String garis() {
        return "=".repeat(50);
    }

    static String garisTipis() {
        return "-".repeat(50);
    }
}