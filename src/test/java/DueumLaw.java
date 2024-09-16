import java.util.Arrays;
import java.util.List;

public class DueumLaw {
    private static final List<Integer> RIEUL_TO_NIEUN = Arrays.asList(4449, 4450, 4457, 4460, 4462, 4467);
    private static final List<Integer> RIEUL_TO_IEUNG = Arrays.asList(4451, 4455, 4456, 4461, 4466, 4469);
    private static final List<Integer> NIEUN_TO_IEUNG = Arrays.asList(4455, 4461, 4466, 4469);

    public static String getSubChar(String mode, String character) {
        String result = null;
        int c = character.codePointAt(0);
        int k;
        int[] ca = new int[3], cb = new int[3];
        boolean cc = false;

        switch (mode) {
            case "EKT": // 영어 끄투
                if (character.length() > 2) result = character.substring(1);
                break;

            case "KKT": // 한국어 쿵쿵따
            case "KSH": // 한국어 끝말잇기
            case "KAP": // 한국어 앞말잇기
                k = c - 0xAC00;
                if (k < 0 || k > 11171) break;

                ca[0] = k / 28 / 21;
                ca[1] = (k / 28) % 21;
                ca[2] = k % 28;

                cb[0] = ca[0] + 0x1100;
                cb[1] = ca[1] + 0x1161;
                cb[2] = ca[2] + 0x11A7;

                if (cb[0] == 4357) { // ㄹ에서 ㄴ, ㅇ
                    cc = true;
                    if (RIEUL_TO_NIEUN.contains(cb[1])) cb[0] = 4354;
                    else if (RIEUL_TO_IEUNG.contains(cb[1])) cb[0] = 4363;
                    else cc = false;
                } else if (cb[0] == 4354) { // ㄴ에서 ㅇ
                    if (NIEUN_TO_IEUNG.contains(cb[1])) {
                        cb[0] = 4363;
                        cc = true;
                    }
                }

                if (cc) {
                    cb[0] -= 0x1100;
                    cb[1] -= 0x1161;
                    cb[2] -= 0x11A7;
                    result = String.valueOf((char) (((cb[0] * 21) + cb[1]) * 28 + cb[2] + 0xAC00));
                }
                break;

            case "ESH": // 영어 끝말잇기
            default:
                break;
        }
        return result;
    }

    public static void main(String[] args) {
        String result = DueumLaw.getSubChar("KKT", "력"); // 테스트로 '가'를 변환
        System.out.println(result);
    }
}
