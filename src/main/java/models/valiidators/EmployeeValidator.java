package models.valiidators;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeView;
import constants.MessageConst;
import services.EmployeeService;

//従業員インスタンスに設定されている値のバリデーションを行うクラス
public class EmployeeValidator {

	/**
	* 従業員インスタンスの各項目についてバリデーションを行う
	* @param service 呼び出し元Serviceクラスのインスタンス
	* @param ev EmployeeViewのインスタンス
	* @param codeDuplicateCheckFlag 社員番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
	* @param passwordCheckFlag パスワードの入力チェックを実施するかどうか(実施する:true 実施しない:false)
	* @return エラーのリスト
	*/

	public static List<String> validate(EmployeeService service, EmployeeView ev, Boolean codeDuplicateCheckFlag,
			Boolean passwordCheckFlag) {
		List<String> errors = new ArrayList<String>();

		//社員番号のチェック
		String codeError = validateCode(service, ev.getCode(), codeDuplicateCheckFlag);
		if (!codeError.equals("")) {
			errors.add(codeError);
		}

		//氏名のチェック
		String nameError = validateName(ev.getName());
		if (!nameError.equals("")) {
			errors.add(nameError);
		}

		//パスワードのチェック
		String passError = validatePassword(ev.getPassword(), passwordCheckFlag);
		if (!passError.equals("")) {
			errors.add(passError);
		}
		return errors;
	}

	//社員番号の入力チェックを行い、エラーメッセージを返却
	/**
	 * @param service EmployeeServiceのインスタンス
     * @param code 社員番号
     * @param codeDuplicateCheckFlag 社員番号の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
	 */
	private static String validateCode(EmployeeService service, String code, Boolean codeDuplicateCheckFlag) {

		//入力値が無ければエラーメッセージを返却
		if (code == null || code.equals("")) {
			return MessageConst.E_NOEMP_CODE.getMessage();
		}

		if (codeDuplicateCheckFlag) {
			//社員番号の重複チェック

			long employeesCount = isDuplicateEmployee(service, code);
			//同一社員番号が既に登録されている場合はエラーメッセージ
			if (employeesCount > 0) {
				return MessageConst.E_EMP_CODE_EXIST.getMessage();
			}
		}

		//エラーなし：空文字
		return "";
	}

	/**
	* @param service EmployeeServiceのインスタンス
	* @param code 社員番号
	* @return 従業員テーブルに登録されている同一社員番号のデータの件数
	*/
	private static long isDuplicateEmployee(EmployeeService service, String code) {
		long employeesCount = service.countByCode(code);
		return employeesCount;
	}

	/**
	 * 氏名に入力値があるかチェックし、なければエラーメッセージ
	 * @param name 氏名
	 * @return エラーメッセージ
	 */
	private static String validateName(String name) {
		if (name == null || name.equals("")) {
			return MessageConst.E_NONAME.getMessage();
		}

		//入力値あり：空文字
		return "";
	}

	/**
	 * パスワードの入力チェックを行い、エラーメッセージを返却
	 * @param password パスワード
	 * @param passwordCheckFlag パスワードの入力チェックを実施
	 * @return エラーメッセージ
	 */
	private static String validatePassword(String password, Boolean passwordCheckFlag) {
		//入力チェックを実施 かつ 入力値が無ければエラーメッセージ
		if (passwordCheckFlag && (password == null || password.equals(""))) {
			return MessageConst.E_NOPASSWORD.getMessage();
		}

		//エラーが無い場合は空文字を返却
		return "";
	}

}
