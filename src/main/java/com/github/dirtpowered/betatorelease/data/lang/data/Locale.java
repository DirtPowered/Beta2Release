package com.github.dirtpowered.betatorelease.data.lang.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Locale {
    AF_ZA("af_za"),
    AR_SA("ar_sa"),
    AST_ES("ast_es"),
    AZ_AZ("az_az"),
    BE_BY("be_by"),
    BG_BG("bg_bg"),
    BR_FR("br_fr"),
    CA_ES("ca_es"),
    CS_CZ("cs_cz"),
    CY_GB("cy_gb"),
    DA_DK("da_dk"),
    DE_ALG("de_alg"),
    DE_AT("de_at"),
    DE_DE("de_de"),
    EL_GR("el_gr"),
    EN_AU("en_au"),
    EN_CA("en_ca"),
    EN_GB("en_gb"),
    EN_NZ("en_nz"),
    EN_PT("en_pt"),
    EN_UD("en_ud"),
    EN_US("en_us"),
    EN_WS("en_ws"),
    EO_UY("eo_uy"),
    ES_AR("es_ar"),
    ES_ES("es_es"),
    ES_MX("es_mx"),
    ES_UY("es_uy"),
    ES_VE("es_ve"),
    ET_EE("et_ee"),
    EU_ES("eu_es"),
    FA_IR("fa_ir"),
    FIL_PH("fil_ph"),
    FI_FI("fi_fi"),
    FO_FO("fo_fo"),
    FR_CA("fr_ca"),
    FR_FR("fr_fr"),
    FY_NL("fy_nl"),
    GA_IE("ga_ie"),
    GD_GB("gd_gb"),
    GL_ES("gl_es"),
    GV_IM("gv_im"),
    HAW_US("haw_us"),
    HE_IL("he_il"),
    HI_IN("hi_in"),
    HR_HR("hr_hr"),
    HU_HU("hu_hu"),
    HY_AM("hy_am"),
    ID_ID("id_id"),
    IO_IDO("io_ido"),
    IS_IS("is_is"),
    IT_IT("it_it"),
    JA_JP("ja_jp"),
    JBO_EN("jbo_en"),
    KA_GE("ka_ge"),
    KO_KR("ko_kr"),
    KSH_DE("ksh_de"),
    KW_GB("kw_gb"),
    LA_LA("la_la"),
    LB_LU("lb_lu"),
    LI_LI("li_li"),
    LOL_US("lol_us"),
    LT_LT("lt_lt"),
    LV_LV("lv_lv"),
    MI_NZ("mi_nz"),
    MK_MK("mk_mk"),
    MN_MN("mn_mn"),
    MS_MY("ms_my"),
    MT_MT("mt_mt"),
    NDS_DE("nds_de"),
    NL_NL("nl_nl"),
    NN_NO("nn_no"),
    NO_NO("no_no"),
    OC_FR("oc_fr"),
    PL_PL("pl_pl"),
    PT_BR("pt_br"),
    PT_PT("pt_pt"),
    QYA_AA("qya_aa"),
    RO_RO("ro_ro"),
    RU_RU("ru_ru"),
    SE_NO("se_no"),
    SK_SK("sk_sk"),
    SL_SI("sl_si"),
    SO_SO("so_so"),
    SQ_AL("sq_al"),
    SR_SP("sr_sp"),
    SV_SE("sv_se"),
    TH_TH("th_th"),
    TLH_AA("tlh_aa"),
    TR_TR("tr_tr"),
    TZL_TZL("tzl_tzl"),
    UK_UA("uk_ua"),
    VAL_ES("val_es"),
    VI_VN("vi_vn"),
    ZH_CN("zh_cn"),
    ZH_TW("zh_tw");

    private final String code;

    public static Locale fromCode(String code) {
        for (Locale locale : values()) {
            if (locale.getCode().equals(code.toLowerCase())) {
                return locale;
            }
        }
        throw new IllegalArgumentException("Unknown locale code: " + code + " Use one of this: \n" + Arrays.toString(values()));
    }
}