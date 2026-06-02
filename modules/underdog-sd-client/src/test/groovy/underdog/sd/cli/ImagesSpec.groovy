package underdog.sd.cli

import underdog.sd.cli.common.SDAwareSpec

class ImagesSpec extends SDAwareSpec {

    def "file to base64 to use in an html img tag"() {
        when:
        String imgAllowedBase64Img = this.alfredHitchcockBase64Image.base64ToBrowserImage()

        then:
        imgAllowedBase64Img.startsWith('data:image/png;base64,')
    }
}
