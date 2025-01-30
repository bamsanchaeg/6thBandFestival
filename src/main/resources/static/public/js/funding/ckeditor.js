import {
	ClassicEditor,
	AccessibilityHelp,
	Autosave,
	Bold,
	Essentials,
	FontBackgroundColor,
	FontColor,
	FontFamily,
	FontSize,
	ImageBlock,
	ImageInline,
	ImageInsert,
	ImageInsertViaUrl,
	ImageTextAlternative,
	ImageToolbar,
	ImageUpload,
	Indent,
	IndentBlock,
	Link,
	List,
	Paragraph,
	PasteFromOffice,
	SelectAll,
	SimpleUploadAdapter,
	Table,
	TableToolbar,
	TextTransformation,
	Underline,
	Undo
} from 'ckeditor5';

let editor;

const editorConfig = {
	toolbar: {
		items: [
			'undo',
			'redo',
			'|',
			'selectAll',
			'|',
			'fontSize',
			'fontFamily',
			'fontColor',
			'fontBackgroundColor',
			'|',
			'bold',
			'underline',
			'|',
			'link',
			'insertImage',
			'insertTable',
			'|',
			'bulletedList',
			'numberedList',
			'indent',
			'outdent',
			'|',
			'accessibilityHelp'
		],
		shouldNotGroupWhenFull: false
	},
	plugins: [
		AccessibilityHelp,
		Autosave,
		Bold,
		Essentials,
		FontBackgroundColor,
		FontColor,
		FontFamily,
		FontSize,
		ImageBlock,
		ImageInline,
		ImageInsert,
		ImageInsertViaUrl,
		ImageTextAlternative,
		ImageToolbar,
		ImageUpload,
		Indent,
		IndentBlock,
		Link,
		List,
		Paragraph,
		PasteFromOffice,
		SelectAll,
		SimpleUploadAdapter,
		Table,
		TableToolbar,
		TextTransformation,
		Underline,
		Undo
	],
	fontFamily: {
		supportAllValues: true
	},
	fontSize: {
		options: [10, 12, 14, 'default', 18, 20, 22],
		supportAllValues: true
	},
	image: {
		toolbar: ['imageTextAlternative']
	},
	// initialData:
		//'<h2>Congratulations on setting up CKEditor 5! 🎉</h2>\n<p>\n    You\'ve successfully created a CKEditor 5 project. This powerful text editor will enhance your application, enabling rich text editing\n    capabilities that are customizable and easy to use.\n</p>\n<h3>What\'s next?</h3>\n<ol>\n    <li>\n        <strong>Integrate into your app</strong>: time to bring the editing into your application. Take the code you created and add to your\n        application.\n    </li>\n    <li>\n        <strong>Explore features:</strong> Experiment with different plugins and toolbar options to discover what works best for your needs.\n    </li>\n    <li>\n        <strong>Customize your editor:</strong> Tailor the editor\'s configuration to match your application\'s style and requirements. Or even\n        write your plugin!\n    </li>\n</ol>\n<p>\n    Keep experimenting, and don\'t hesitate to push the boundaries of what you can achieve with CKEditor 5. Your feedback is invaluable to us\n    as we strive to improve and evolve. Happy editing!\n</p>\n<h3>Helpful resources</h3>\n<ul>\n    <li>📝 <a href="https://orders.ckeditor.com/trial/premium-features">Trial sign up</a>,</li>\n    <li>📕 <a href="https://ckeditor.com/docs/ckeditor5/latest/installation/index.html">Documentation</a>,</li>\n    <li>⭐️ <a href="https://github.com/ckeditor/ckeditor5">GitHub</a> (star us if you can!),</li>\n    <li>🏠 <a href="https://ckeditor.com">CKEditor Homepage</a>,</li>\n    <li>🧑‍💻 <a href="https://ckeditor.com/ckeditor-5/demo/">CKEditor 5 Demos</a>,</li>\n</ul>\n<h3>Need help?</h3>\n<p>\n    See this text, but the editor is not starting up? Check the browser\'s console for clues and guidance. It may be related to an incorrect\n    license key if you use premium features or another feature-related requirement. If you cannot make it work, file a GitHub issue, and we\n    will help as soon as possible!\n</p>\n',
        // '<h2>안녕하세요. \n프로젝트 작성을 시작한 창작자님 환영해요!</h2>',
    // simpleUpload: {
    //     // 업로드 URL 설정 필요
    //     uploadUrl: '/upload/image',
        
    //     // 업로드 시 추가할 HTTP 헤더
    //     headers: {
    //         'X-CSRF-Token': 'CSRF-Token',
    //     }
    // },
    language: 'ko',
	link: {
		addTargetToExternalLinks: true,
		defaultProtocol: 'https://',
		decorators: {
			toggleDownloadable: {
				mode: 'manual',
				label: 'Downloadable',
				attributes: {
					download: 'file'
				}
			}
		}
	},
	placeholder: 'Type or paste your content here!',
	table: {
		contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells']
	}
};

// ClassicEditor.create(document.querySelector('#editor'), editorConfig);

document.addEventListener("DOMContentLoaded", function() {
	ClassicEditor
		.create(document.querySelector('#ckeditor1'), editorConfig)
        .then(newEditor => {
            editor = newEditor;
        })
		.catch(error => {
			console.error(error);
		});

});

export function projectStart() {
    if (editor) {
        // CKEditor의 내용을 <textarea>에 업데이트
        document.querySelector('#ckeditor1').value = editor.getData();

        const form = document.getElementById("peditor-form");
        const formData = new FormData(form);

        // formdata 확인..
        for (const x of formData.entries()) {
            console.log(x);
        }
        // return;

        const url = "/api/funding/updateProject";
        fetch(url, {
            method: "post",
            body: formData
        })
        .then(response => response.json())
        .then((response) => {
            if(response.result === 'success') {
                console.log("수정 완료");
            } else {
                console.log("수정 실패");
            }
        });
    } else {
        console.error('Editor is not initialized');
    }
}
