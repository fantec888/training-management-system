export function toPercent(value) {
  if (value === null || value === undefined || value === '') {
    return '0%'
  }
  const number = Number(value)
  return Number.isFinite(number) ? `${number.toFixed(1).replace(/\.0$/, '')}%` : `${value}%`
}

export function toCurrency(value) {
  const number = Number(value)
  if (!Number.isFinite(number)) {
    return `¥${value}`
  }
  return `¥${number.toFixed(2)}`
}
