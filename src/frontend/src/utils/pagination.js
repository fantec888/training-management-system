import { computed, ref, watch } from 'vue'

export function useClientPagination(source, options = {}) {
  const page = ref(options.page || 1)
  const pageSize = ref(options.pageSize || 5)
  const pageSizes = options.pageSizes || [5, 10, 20, 50]

  const total = computed(() => source.value.length)
  const records = computed(() => {
    const start = (page.value - 1) * pageSize.value
    return source.value.slice(start, start + pageSize.value)
  })

  watch([source, pageSize], () => {
    page.value = 1
  })

  return {
    page,
    pageSize,
    pageSizes,
    total,
    records,
  }
}
