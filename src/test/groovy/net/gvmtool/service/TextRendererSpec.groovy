package net.gvmtool.service

import net.gvmtool.domain.Broadcast
import spock.lang.Specification

import static net.gvmtool.service.TextRenderer.*

class TextRendererSpec extends Specification {

    TextRenderer renderer

    Broadcast broadcast1 = new Broadcast(id: 1, text: "text1", date: new Date())
    Broadcast broadcast2 = new Broadcast(id: 2, text: "text2", date: new Date())

    void setup() {
        renderer = new TextRenderer()
    }

    void "should add header line when preparing broadcast text"() {
        given:
        def broadcasts = [broadcast1, broadcast2]

        when:
        def text = renderer.prepare(broadcasts)
        def firstLine = text.readLines().first()

        then:
        firstLine == '==== BROADCAST ================================================================='
    }

    void "should add footer line when preparing broadcast text"() {
        given:
        def broadcasts = [broadcast1, broadcast2]

        when:
        def text = renderer.prepare(broadcasts)
        def lastLine = text.readLines().last()

        then:
        lastLine == '================================================================================'
    }

    void "should place each string on a new line"() {
        given:
        def broadcasts = [broadcast1, broadcast2]

        when:
        def text = renderer.prepare(broadcasts)
        def readLines = text.readLines()

        then:
        readLines.size() == 4
        readLines[0] == HEADER
        readLines[1].contains(broadcast1.text)
        readLines[2].contains(broadcast2.text)
        readLines[3] == FOOTER
    }

    void "should allow conversion of single broadcasts"() {
        when:
        def text = renderer.prepare(broadcast1)
        def readLines = text.readLines()

        then:
        readLines.size() == 3
        readLines[0] == HEADER
        readLines[1].contains(broadcast1.text)
        readLines[2] == FOOTER
    }

    void "should render a friendly message if no broadcasts are passed in"() {
        when:
        def text = renderer.prepare([])
        def readLines = text.readLines()

        then:
        readLines[0] == HEADER
        readLines[1] == "No broadcasts available at present."
        readLines[2] == FOOTER
    }

}