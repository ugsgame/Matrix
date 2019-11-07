/****************************************************************************
Copyright (c) 2011-2013,WebJet Business Division,CYOU
 
http://www.genesis-3d.com.cn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#ifndef __MONO_API_H__
#define __MONO_API_H__

#include "MonoTypes.h"

// - in order to make a dynamic link with mono.dll, I define mono API like this
// - so if you want to invoke a mono function, you shall make a sentence like the follow,that is all

// UNFOLD( void, mono_domain_try_unload, (MonoDomain *domain, MonoObject **exc));
// UNFOLD( gpointer, mono_method_get_unmanaged_thunk, (MonoMethod *method));

// - make all api in a macro,thus,they can be repeat just writing a macro
#define ALL_MONO_API(UNFOLD) \
UNFOLD( MonoDomain*, mono_jit_init_version, (const char *root_domain_name, const char *runtime_version));\
UNFOLD( void, mono_jit_parse_options, (int argc, char* argv[]) );\
UNFOLD( void, mono_jit_cleanup, (MonoDomain *domain)); \
UNFOLD( int, mono_jit_exec, (MonoDomain *domain, MonoAssembly *assembly, int argc, char *argv[]));\
UNFOLD( MonoObject*, mono_object_new, (MonoDomain* domain, MonoClass* monoClass) );\
UNFOLD( MonoDomain*, mono_object_get_domain,(MonoObject *obj));\
UNFOLD( void, mono_raise_exception,(MonoException *ex));\
UNFOLD( void, mono_runtime_object_init, (MonoObject* monoObj));\
UNFOLD( MonoObject*, mono_runtime_invoke_array, (MonoMethod *method, void *obj, MonoArray *params,MonoObject **exc));\
UNFOLD( MonoDomain*, mono_domain_get,(void) );\
UNFOLD( gboolean,mono_domain_set,(MonoDomain *domain, gboolean force));\
UNFOLD( MonoDomain *, mono_domain_create_appdomain,(const char *domainname, const char* configfile));\
UNFOLD( void, mono_debug_open_image_from_memory,(MonoImage *image, const char *raw_contents, int size));\
UNFOLD( void, mono_domain_unload, (MonoDomain* domain));\
UNFOLD( MonoDomain*, mono_get_root_domain,(void));\
UNFOLD( mono_bool, mono_domain_finalize, (MonoDomain *domain, uint32_t timeout));\
UNFOLD( MonoImage*, mono_get_corlib, (void));\
UNFOLD( MonoClass*, mono_get_object_class,(void));\
UNFOLD( MonoClass*, mono_get_exception_class,(void));\
UNFOLD( void, mono_set_dirs, (const char* assembly_dir, const char* config_dir) );\
UNFOLD( void, mono_set_assemblies_path, (const char* path) );\
UNFOLD( void, mono_debug_init, (MonoDebugFormat format) );\
UNFOLD( MonoThread*, mono_thread_current, (void) );\
UNFOLD( void, mono_thread_set_main, (MonoThread* thread) );\
UNFOLD( MonoThread* ,mono_thread_attach, (MonoDomain *domain));\
UNFOLD( void, mono_thread_detach, (MonoThread *thread));\
UNFOLD( MonoMethodDesc*, mono_method_desc_new, (const char* name, gboolean include_namespace) );\
UNFOLD( void,	mono_method_desc_free,(MonoMethodDesc *desc) );\
UNFOLD( MonoMethod*, mono_method_desc_search_in_class, (MonoMethodDesc* desc, MonoClass* monoClass) );\
UNFOLD( MonoMethod*, mono_method_get_last_managed, ());\
UNFOLD( const char*, mono_method_get_name,(MonoMethod *method));\
UNFOLD( MonoClass*, mono_method_get_class,(MonoMethod *method));\
UNFOLD( MonoMethodSignature*, mono_method_signature, (MonoMethod *method));\
UNFOLD( mono_bool, mono_class_is_valuetype, (MonoClass* klass) );\
UNFOLD( void*, mono_object_unbox,(MonoObject* pObj) );\
UNFOLD( MonoObject*, mono_runtime_invoke, (MonoMethod* method, void* obj, void** params, MonoObject** exc) );\
UNFOLD( char*, mono_string_to_utf8,(MonoString* string_obj) );\
UNFOLD( mono_unichar2*, mono_string_to_utf16,(MonoString* string_obj) );\
UNFOLD( mono_bool, mono_class_is_subclass_of, (MonoClass* klass, MonoClass* klassc, mono_bool check_interfaces) );\
UNFOLD( MonoMethod*, mono_class_get_method_from_name, (MonoClass* klass, const char* name, int param_count) );\
UNFOLD( MonoImage*, mono_class_get_image,(MonoClass *klass));\
UNFOLD( int32_t, mono_array_element_size, (MonoClass *ac));\
UNFOLD( MonoClass*, mono_class_get_element_class, (MonoClass *klass));\
UNFOLD( MonoClass*, mono_class_get_parent, (MonoClass* klass) );\
UNFOLD( MonoClass*, mono_class_from_name, (MonoImage* image, const char* name_space, const char* name) );\
UNFOLD( MonoClass*, mono_array_class_get, (MonoClass *element_class, uint32_t rank));\
UNFOLD( const char*, mono_class_get_name, (MonoClass *klass) );\
UNFOLD( const char*, mono_class_get_namespace, (MonoClass *klass));\
UNFOLD( MonoType*, mono_class_get_type, (MonoClass *klass));\
UNFOLD( MonoMethod*, mono_class_get_methods, (MonoClass* klass, void **iter));\
UNFOLD( MonoProperty*, mono_class_get_properties, (MonoClass* klass, void **iter));\
UNFOLD( MonoClass*, mono_class_get_interfaces,(MonoClass* klass, void **iter));\
UNFOLD( const char*, mono_property_get_name, (MonoProperty *prop));\
UNFOLD( MonoMethod*, mono_property_get_set_method, (MonoProperty *prop));\
UNFOLD( MonoMethod*, mono_property_get_get_method, (MonoProperty *prop));\
UNFOLD( MonoClass*, mono_property_get_parent,(MonoProperty *prop));\
UNFOLD( uint32_t, mono_property_get_flags, (MonoProperty *prop));\
UNFOLD( MonoAssembly*, mono_domain_assembly_open, (MonoDomain* domain, const char* name) );\
UNFOLD( MonoImage*,mono_image_open_from_data_full,(const void *data, guint32 data_len, gboolean need_copy, int *status, gboolean ref_only));\
UNFOLD( void,mono_image_close,(MonoImage *image));\
UNFOLD( const char*, mono_image_get_name, (MonoImage *image));\
UNFOLD( const char*, mono_image_get_filename, (MonoImage *image));\
UNFOLD( MonoAssembly*, mono_image_get_assembly, (MonoImage *image));\
UNFOLD( MonoAssembly*,mono_assembly_load_from_full,(MonoImage *image, const char *fname,int *status,gboolean refonly));\
UNFOLD( MonoImage*, mono_assembly_get_image, (MonoAssembly* assembly) );\
UNFOLD( MonoAssembly*,mono_assembly_get_main,());\
UNFOLD( MonoReflectionAssembly*, mono_assembly_get_object,(MonoDomain *domain, MonoAssembly *assembly));\
UNFOLD( uint32_t, mono_gchandle_new, (MonoObject* obj, mono_bool pinned) );\
UNFOLD( void, mono_gchandle_free, (uint32_t gchandle) );\
UNFOLD( guint32, mono_gchandle_new_weakref, (MonoObject *obj, gboolean track_resurrection)  );\
UNFOLD( MonoObject* , mono_gchandle_get_target , (uint32_t gchandle) );\
UNFOLD( void, mono_gc_collect, (int generation) );\
UNFOLD( int, mono_gc_max_generation, () );\
UNFOLD( gboolean,mono_object_is_alive ,(MonoObject* o));\
UNFOLD( void, mono_add_internal_call, (const char *name, const void* method));\
UNFOLD( MonoString*, mono_string_new, (MonoDomain *domain, const char *text));\
UNFOLD( MonoString*, mono_string_new_wrapper, (const char* text) );\
UNFOLD( MonoString*, mono_string_new_utf16, (MonoDomain* domain, const mono_unichar2 *text, int32_t len));\
UNFOLD( MonoObject*, mono_value_box, (MonoDomain *domain, MonoClass *klass, void* val));\
UNFOLD( MonoClass*, mono_get_byte_class, (void) );\
UNFOLD( MonoClass*, mono_get_boolean_class, (void));\
UNFOLD( MonoClass*, mono_get_int32_class, (void));\
UNFOLD( MonoClass*, mono_get_uint32_class, (void));\
UNFOLD( MonoClass*, mono_get_intptr_class, (void));\
UNFOLD( MonoClass*, mono_get_uintptr_class, (void));\
UNFOLD( MonoClass*, mono_get_int16_class, (void));\
UNFOLD( MonoClass*, mono_get_uint16_class, (void));\
UNFOLD( MonoClass*, mono_get_single_class, (void));\
UNFOLD( MonoClass*, mono_get_string_class, (void) );\
UNFOLD( MonoClass*, mono_get_array_class, (void));\
UNFOLD( char*, mono_array_addr_with_size, (MonoArray* array, int size, uintptr_t idx) );\
UNFOLD( MonoArray*, mono_array_new, (MonoDomain* domain, MonoClass* eclass, uintptr_t n) );\
UNFOLD( char*, mono_array_clone,(MonoArray *array));\
UNFOLD( MonoClass*, mono_object_get_class,(MonoObject* obj) );\
UNFOLD( MonoClassField*, mono_class_get_fields,(MonoClass* klass, gpointer* iter) );\
UNFOLD( const char*, mono_field_get_name, (MonoClassField* field) );\
UNFOLD( MonoType*, mono_field_get_type, (MonoClassField* field) );\
UNFOLD( guint32, mono_field_get_flags, (MonoClassField* field) );\
UNFOLD( int, mono_type_get_type, (MonoType* type) );\
UNFOLD( MonoClass* ,mono_type_get_class, (MonoType *type) );\
UNFOLD( guint32, mono_field_get_offset, (MonoClassField *field) );\
UNFOLD( void, mono_field_get_value, (MonoObject *obj, MonoClassField *field, void *value) );\
UNFOLD( void, mono_field_set_value, (MonoObject *obj, MonoClassField *field, void *value) );\
UNFOLD( MonoObject*, mono_field_get_value_object, (MonoDomain *domain, MonoClassField *field, MonoObject *obj));\
UNFOLD( void, mono_property_set_value, (MonoProperty *prop, void *obj, void **params, MonoObject **exc));\
UNFOLD( MonoObject*, mono_property_get_value, (MonoProperty *prop, void *obj, void **params, MonoObject **exc));\
UNFOLD( MonoType*, mono_signature_get_params, (MonoMethodSignature *sig, void **iter));\
UNFOLD( uint32_t, mono_signature_get_param_count, (MonoMethodSignature *sig));\
UNFOLD( MonoException *, mono_exception_from_name, (MonoImage *image, const char* name_space, const char *name););\
UNFOLD( MonoException *, mono_exception_from_name_msg, (MonoImage *image, const char *name_space,const char *name, const char *msg));\
UNFOLD( void, mono_security_enable_core_clr, (void) );\
typedef bool (*MonoCoreClrPlatformCB) (const char *image_name);\
UNFOLD( bool,mono_security_set_core_clr_platform_callback, (MonoCoreClrPlatformCB));\
UNFOLD( void, g_free,(void*));\
// - NOTE:don't forget to put a slash'\'

// -
#ifndef __OSX__
#define MONO_API_EXTERN(ret,fun,params)	extern ret (*fun) params;
#else
#ifdef __cplusplus
#define MONO_API_EXTERN(ret,fun,params)	extern "C" ret fun params;
extern "C" void ManuallyCollection( float );
#else
#define MONO_API_EXTERN(ret,fun,params)	extern ret fun params;
extern void ManuallyCollection( float );
#endif
#endif
ALL_MONO_API(MONO_API_EXTERN)
#endif
