package org.wheat.leaflets.entity;
/**
 * 
* @ClassName: ConstantValue 
* @Description: TODO 
��ȡ����ʧ�����֣�

1.���ݿ��������
2.���ݿⲻ��������
3.����ʧ��

�ϴ�/��������ʧ������
1.���ݿ��������
2.����ʧ��
* @author hogachen
* @date 2014��12��12�� ����6:30:01 
*
 */
public class ConstantValue {
	public static String HttpRoot="http://120.24.161.139:8080/BeautyRankingServer/";
	/**
	 * ʧ�ܵ�������ǧ���֣��ɹ�������ֻ��һ�֣�����ʧ�ܣ�
	 */
	//ʧ��״̬
	public static int ClientParameterErr=1001;//�ͻ��������������

	public static int InsertDbFailed=1002;//���ݿ����ʧ��

	public static int uploadPhotoFailed=1004;

	public static int updateCommentCountFailed=1009;
	
	public static int dataNotInDBCannotUpdate=1006;

	public static int getDataFailed=1010;

	public static int deleRecordNotExist=1012;//Ҫɾ���ļ�¼������


	public static int delBeautyButNotPhoto=1017;
	
	public static int SocketTimeoutException=1018;//�������ݳ�ʱ
	
	public static int ReUploadFailed=1019;
	public static int ReUploadSuccess=1020;
	
	
	/**
	 * ��ȡ����ʧ�ܾ�������
	 */
	//ʧ��״̬
	public static int DBException=1013;//���ݿ�ִ��SQL���������
	
	public static int DataNotFoundInDB=1014;//���ݿ�û���������
	
	//�ɹ�״̬
	public static int operateSuccess=1000;
}