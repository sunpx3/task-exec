package com.sunpx.quartz_one.httputil;

public class CookiesUtil {

	public static String getCookies() {
		
		//拼接cookies
		StringBuffer cookies = new StringBuffer();
		
//		cookies.append("BDUSS=B2OUdveWZnSH5-MDRIT0ZYU3UydUc1b2hvNUxtV1R3cHFnNEx-akRxdWhIenhiQVFBQUFBJ"
//					 + "CQAAAAAAAAAAAEAAADtLYY1yfHW3bulwapmcmVlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKGSFFuhkhRbNj;");
//		cookies.append("BAIDUID=BE0FE1EA85462469834C2CBA9236118A;");

		cookies.append("BAIDUID=BE0FE1EA85462469834C2CBA9236118A:FG=1; MBD_AT=1527507022; FP_UID=c6ca632a21c4d9019519c4ed31f035d8; BDUSS=B2OUdveWZnSH5-MDRIT0ZYU3UydUc1b2hvNUxtV1R3cHFnNEx-akRxdWhIenhiQVFBQUFBJCQAAAAAAAAAAAEAAADtLYY1yfHW3bulwapmcmVlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKGSFFuhkhRbNj; BAIDU_WISE_UID=wapp_1528170541453_739; BDPASSGATE=IlPT2AEptyoA_yiU4VGG3kIN8enzKP_B1hHDSEpxR6ePpiyWmhHoB_2EUjD6YnSgBC3gzDCweMpkkUXKXlVXcqFCh4sAmWlubFu6wNb6xsOAGBRAzbIZCb4jKUg2p3XvhBEax3ET0QFX4G9FbQDxpuo4ivKl7AhMae8R7802g2XuBlOR2Y4_zWyaOolmO-0APNu5cPrljSgdPk_cWe8oRi_2gC1iVp1u8bHbjroaOfD-qncL1tbtZ1FL1Fz51JZ3XQi9H4WU71O8X55UvWVYVlIL-5SI5qnxR5-Q2dTdfM2JQr8hPcerMSjuHqcdjbzfRB-hQhasbsEUPTES8ZcxGWxmRK8zID01SUwzW2ORgA4DHXxkqjW_BhnYyWU5ZKjH0wkSU3VUDkVMmC_CveDK; H_WISE_SIDS=109776_122155_120187_107317_118887_118864_118839_118820_118804_117332_124062_117436_124071_123985_122790_123842_123813_120549_123808_123782_123699_123980_124030_110086_123290; PSINO=2; WISE_HIS_PM=0; BAIDUCUID=luHuuguKSu_98v8Xgi2Ri_O6HugcOHur0uvt8g812ui9aB8g_a2AtgP62iAqToFoC");
		return cookies.toString();
	}
}
