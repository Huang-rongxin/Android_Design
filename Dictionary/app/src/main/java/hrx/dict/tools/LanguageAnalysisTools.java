package hrx.dict.tools;

public class LanguageAnalysisTools {
	/**
	 * chinese is 0  ;
	 */
	public static  final int CHINESE = 0;
	/**
	 * english is 1;
	 */
	public static  final int ENGLISH = 1;
	//

	public static int getLanguage(String inputStr)
	{
		char c = inputStr.charAt(0);// �ж�һ���ַ��Ƿ�������
		if(c>=0x4e00 && c<=0x9fa5){
			return CHINESE ;
		}//����
		else{
			return ENGLISH ;
		}//���Ǻ���
	}
}
