package falih.example.bukuwarungtestapp.ui.common.adapter

interface BindableAdapterFunctions<T> {
    fun setItems(data: T?)
    fun addItems(data: T)
}