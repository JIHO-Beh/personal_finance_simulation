import * as yup from 'yup'

const labelText = (prm: { label?: string }) => {
  return prm.label ? `${prm.label}は` : '';
}

const jpConfig = {
  mixed: {
    default: (prm: { label?: string }) => `${labelText(prm)}無効です`,
    required: (prm: { label?: string }) => `${labelText(prm)}必須の入力項目です`,
    oneOf: (prm: { label?: string; values?: any }) => `${labelText(prm)}次の値のいずれかでなければなりません:${prm.values}`,
    notOneOf: (prm: { label?: string; values?: any }) => `${labelText(prm)}次の値のいずれかであってはなりません:${prm.values}`,
    notType: `形式が違います`,
    defined: ``
  },
  string: {
    length: (prm: { label?: string; length: number }) => `${labelText(prm)}${prm.length}文字でなければなりません`,
    min: (prm: { label?: string; min: number }) => `${labelText(prm)}少なくとも${prm.min}文字でなければなりません`,
    max: (prm: { label?: string; max: number }) => `${labelText(prm)}最大${prm.max}文字でなければなりません`,
    matches: (prm: { label?: string; regex: RegExp }) => `${labelText(prm)}次の形式と一致する必要があります: "${prm.regex}"`,
    email: (prm: { label?: string; regex: RegExp }) => `${labelText(prm)}メールアドレス形式である必要があります`,
    url: (prm: { label?: string; regex: RegExp }) => `${labelText(prm)}有効なURLでなければなりません`,
    uuid: (prm: { label?: string; regex: RegExp }) => `${labelText(prm)}有効なUUIDでなければなりません`,
    trim: (prm: { label?: string }) => `${labelText(prm)}前後にスペースを入れてはいけません`,
    lowercase: (prm: { label?: string }) => `${labelText(prm)}小文字でなければなりません`,
    uppercase: (prm: { label?: string }) => `${labelText(prm)}大文字でなければなりません`,
  },
  number: {
    min: (prm: { label?: string; min: number }) => `${labelText(prm)}${prm.min}以上である必要があります`,
    max: (prm: { label?: string; max: number }) => `${labelText(prm)}${prm.max}以下でなければなりません`,
    lessThan: (prm: { label?: string; less: number }) => `${labelText(prm)}${prm.less}より小さくなければなりません`,
    moreThan: (prm: { label?: string; more: number }) => `${labelText(prm)}${prm.more}より大きくなければなりません`,
    positive: (prm: { label?: string; more?: number }) => `${labelText(prm)}正の数でなければなりません`, // more はオプション
    negative: (prm: { label?: string; less?: number }) => `${labelText(prm)}負の数でなければなりません`, // less はオプション
    integer: (prm: { label?: string }) => `${labelText(prm)}整数でなければなりません`,
  },
  date: {
    min: (prm: { label?: string; min: Date | string }) => `${labelText(prm)}${prm.min}より後でなければなりません`,
    max: (prm: { label?: string; max: Date | string }) => `${labelText(prm)}${prm.max}より前でなければなりません`,
  },
  boolean: {
    isValue: (prm: { label?: string }) => `${labelText(prm)}値が必要です`,
  },
  object: {
    noUnknown: (prm: { label?: string }) => `${labelText(prm)}オブジェクトシェイプで指定されていないキーを含めることはできません`,
  },
  array: {
    length: (prm: { label?: string; length: number }) => `${labelText(prm)}${prm.length}個が必要です`,
    min: (prm: { label?: string; min: number }) => `${labelText(prm)}${prm.min}個以上の項目が必要です`,
    max: (prm: { label?: string; max: number }) => `${labelText(prm)}${prm.max}個以下の項目が必要です`,
  },
}

yup.setLocale(jpConfig)
export default yup